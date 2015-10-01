package com.aphoh.muser.music

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.IBinder
import android.os.PowerManager
import android.util.SparseArray
import com.aphoh.muser.App
import com.aphoh.muser.data.db.model.SongItem
import com.aphoh.muser.util.LogUtil
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Will on 7/12/15.
 */
public class MusicService() : Service() {
    private val log = LogUtil(MusicService::class.java.simpleName)

    private var mDataInteractor = App.applicationComponent.interactor()
    private var mMediaPlayer = MediaPlayer()
    private var mSongs: MutableList<SongItem> = ArrayList()
    private var mIndex = 0
    private val mBinder = NotificationBinder()
    var views = SparseArray<MusicView>()
    var tickerSub: Subscription? = null

    override fun onCreate() {
        super.onCreate()
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mMediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK)
    }

    override fun onBind(intent: Intent?): IBinder = mBinder

    public inner class NotificationBinder() : android.os.Binder() {
        init {
            log.d("Created Notification binder")
        }

        public var service: MusicService = this@MusicService

    }

    public fun bind(id: Int, musicView: MusicView) {
        views.put(id, musicView)
    }

    public fun unbind(id: Int) {
        views.remove(id)
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
            mDataInteractor.requestUrlForSongItem(item)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe (
                            { item ->
                                mSongs.set(mIndex, item)
                                playSong(mIndex)
                            },
                            { error ->
                                mIndex += 1
                                playSong(mIndex)
                            })
        } else {
            mMediaPlayer.setDataSource(item.streamUrl)
            mMediaPlayer.setOnPreparedListener {
                log.d("Prepared, playing...")
                mMediaPlayer.start()
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
                mIndex += 1
                if (mSongs.size() < mIndex) playSong(mIndex)
            }
            mMediaPlayer.setOnErrorListener { mediaPlayer, what, extra ->
                tickerSub?.unsubscribe()
                log.e("Error in mediaPlayer $what-$extra")
                doOnViews { it.publishError("Error playing media: $what-$extra") }
                true
            }
            mMediaPlayer.prepareAsync()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tickerSub?.unsubscribe()
    }

    private fun doOnViews(operation: (MusicView) -> Unit) {
        for (i in 0..views.size() - 1) operation.invoke(views.valueAt(i))
    }


    public fun pause() {
        if (mMediaPlayer.isPlaying)
            mMediaPlayer.pause()
    }

    public fun resume() {
        if (!mMediaPlayer.isPlaying)
            mMediaPlayer.start()
    }

    public fun stop() {
        mMediaPlayer.reset()
    }


    companion object IntentFactory {
        public fun getIntent(context: Context): Intent = Intent(context, MusicService::class.java)
    }
}