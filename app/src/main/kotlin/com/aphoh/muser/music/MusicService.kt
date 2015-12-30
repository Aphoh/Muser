package com.aphoh.muser.music

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.IBinder
import android.os.PowerManager
import android.util.SparseArray
import com.aphoh.muser.App
import com.aphoh.muser.data.db.model.SongItem
import com.aphoh.muser.ui.view.ControlsView
import com.aphoh.muser.util.LogUtil
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Will on 7/12/15.
 */
public class MusicService() : Service(), AudioManager.OnAudioFocusChangeListener {
    private val log = LogUtil(MusicService::class.java.simpleName)

    private var mDataInteractor = App.applicationComponent.interactor()
    private var mMediaPlayer = MediaPlayer()
    private var mSongs: MutableList<SongItem> = ArrayList()
    private var mIndex = 0
    private val mBinder = NotificationBinder()
    private val mNoisyReciever = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            pause()
        }
    }
    val views = SparseArray<MusicView>()
    val pauseables = ArrayList<ControlsView>()
    var tickerSub: Subscription? = null

    private var mCurrentSong: SongItem? = null
    private var mCurrentProgress = -1

    override fun onCreate() {
        super.onCreate()
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mMediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK)

        val filter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
        registerReceiver(mNoisyReciever, filter)
    }

    override fun onBind(intent: Intent?): IBinder = mBinder

    public inner class NotificationBinder() : android.os.Binder() {
        init {
            log.d("Created Notification binder")
        }

        public var service: MusicService = this@MusicService

    }

    public fun isBound(musicView: MusicView): Boolean {
        return views.get(musicView.id, null) != null
    }

    public fun isBound(controlsView: ControlsView): Boolean {
        return pauseables.contains(controlsView)
    }

    public fun bind(controlsView: ControlsView) {
        pauseables.add(controlsView)
        controlsView.removeCallbacks()
        controlsView.addPlayPauseCallback {
            if (it) resume() else pause()
        }
    }

    public fun unbind(controlsView: ControlsView) {
        pauseables.remove(controlsView)
        controlsView.removeCallbacks()
    }

    public fun bind(musicView: MusicView) {
        views.put(musicView.id, musicView)
        mCurrentSong?.let {
            musicView.publishAlbumArt(it.image)
            musicView.publishSongArtist(it.artist)
            musicView.publishProgress(mCurrentProgress)
            musicView.publishSongName(it.songTitle)
        }
    }

    public fun unbind(musicView: MusicView) {
        views.remove(musicView.id)
    }

    public fun playSongs(songItems: List<SongItem>) {
        log.d("PlaySongs called with songs: $songItems")
        mSongs = ArrayList(songItems)
        if (!mSongs.isEmpty()) {
            mIndex = 0
            playSong(mIndex)
        }
    }

    public fun playSong(index: Int) {
        var item = mSongs.get(index)
        doOnViews { view ->
            view.publishProgress(-1)
            view.publishAlbumArt(item.image)
            view.publishSongName(item.songTitle)
            view.publishSongArtist(item.artist)
        }

        if (item.streamUrl == null) {
            log.d("Stream url was null")
            mDataInteractor.requestUrlForSongItem(item)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe (
                            { item ->
                                log.d("Retrieved stream url, playing...")
                                mSongs.set(mIndex, item)
                                playSong(mIndex)
                            },
                            { error ->
                                log.e("Error getting stream url", error)
                                mIndex += 1
                                playSong(mIndex)
                            })
        } else {
            mCurrentSong = item
            log.d("Stream url was not null")
            mMediaPlayer.reset()
            tickerSub?.unsubscribe()
            mMediaPlayer.setDataSource(item.streamUrl)
            mMediaPlayer.setOnPreparedListener {
                log.d("Prepared, playing...")
                withPlay { mMediaPlayer.start() }
                doOnPauseables { it.playing = true }
                tickerSub = rx.Observable.interval(200, TimeUnit.MILLISECONDS)
                        .repeat()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            log.d("Emitted long $it")
                            var position = (mMediaPlayer.currentPosition.toDouble() / 1000.0).toInt()
                            doOnViews { it.publishProgress(position) }
                        }
            }
            mMediaPlayer.setOnCompletionListener {
                tickerSub?.unsubscribe()
                doOnPauseables { it.playing = false }
                next()
            }
            mMediaPlayer.setOnErrorListener { mediaPlayer, what, extra ->
                tickerSub?.unsubscribe()
                log.e("Error in mediaPlayer $what-$extra")
                doOnPauseables { it.playing = false }
                doOnViews { it.publishError("Error playing media: $what-$extra") }
                true
            }
            mMediaPlayer.prepareAsync()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tickerSub?.unsubscribe()
        stop()
    }

    private fun withPlay(action: () -> Unit) {
        val am = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val result = am.requestAudioFocus(this,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN)
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) action.invoke()
    }

    private fun withPause(action: () -> Unit) {
        unregisterReceiver(mNoisyReciever)
        action.invoke()
    }

    private fun withStop(action: () -> Unit) {
        (getSystemService(Context.AUDIO_SERVICE) as AudioManager).abandonAudioFocus(this)
        action.invoke()
    }

    override fun onAudioFocusChange(focusChange: Int) {
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                mMediaPlayer.start()
                mMediaPlayer.setVolume(1.0f, 1.0f)
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> pause()
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> mMediaPlayer.setVolume(0.5f, 0.5f)
            AudioManager.AUDIOFOCUS_LOSS -> mMediaPlayer.stop()
        }
    }

    /*
    * Util with functions
    * */

    private fun doOnViews(operation: (MusicView) -> Unit) {
        for (i in 0..views.size() - 1) operation.invoke(views.valueAt(i))
    }

    private fun doOnPauseables(operation: (ControlsView) -> Unit) {
        for (pauseable in pauseables) operation.invoke(pauseable)
    }

    public fun pause() {
        withPause {
            if (mMediaPlayer.isPlaying) {
                mMediaPlayer.pause()
            }
        }
    }

    public fun resume() {
        withPlay {
            if (!mMediaPlayer.isPlaying) {
                mMediaPlayer.start()
            }
        }
    }

    public fun stop() {
        withStop {
            mMediaPlayer.stop()
        }
    }

    public fun next() {
        mIndex += 1
        if (mIndex < mSongs.size) playSong(mIndex)
    }

    public fun isPlaying(mSongItem: SongItem): Boolean = mSongItem.id == mCurrentSong?.id

    companion object IntentFactory {
        public fun getIntent(context: Context): Intent = Intent(context, MusicService::class.java)
    }
}