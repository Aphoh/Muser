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
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaButtonReceiver
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.aphoh.muser.App
import com.aphoh.muser.data.db.model.SongItem
import com.aphoh.muser.util.LogUtil
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Will on 7/12/15.
 */
public class MusicService() : Service(), AudioManager.OnAudioFocusChangeListener {
    private val PLAYBACK_SPEED_PAUSED = 0f
    private val PLAYBACK_SPEED_PLAYING = 1.0f

    private val log = LogUtil(MusicService::class.java.simpleName)

    private var mDataInteractor = App.applicationComponent.interactor()
    private var mMediaPlayer = MediaPlayer()
    private var mSongs: MutableList<SongItem> = ArrayList()
    private var mIsPrepared = false

    private var mIndex = 0
    private val mBinder = NotificationBinder()
    private val mNoisyReciever = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            pause()
        }
    }
    private val mMediaSession = lazy { MediaSessionCompat(this, MusicService::class.java.simpleName) }
    var tickerSub: Subscription? = null

    val mPlaybackState = PlaybackStateCompat.Builder()

    public var mCurrentSong: SongItem? = null

    private val mNoisyFilter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
    private var mNotification: MediaNotificationManager? = null

    override fun onCreate() {
        super.onCreate()
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mMediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK)

        mMediaSession.value.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                        .or(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS)
        )

        mMediaSession.value.setCallback(callbacks)

        mPlaybackState.setActions(0)
        mPlaybackState.setState(PlaybackStateCompat.STATE_NONE, 0, 0.toFloat())
        publishPlaybackState()

        mNotification = MediaNotificationManager(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        MediaButtonReceiver.handleIntent(mMediaSession.value, intent)
        return Service.START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder = mBinder

    public inner class NotificationBinder() : android.os.Binder() {
        init {
            log.d("Created Notification binder")
        }

        public var service: MusicService = this@MusicService

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

        if (item.streamUrl == null) {
            log.d("Stream url was null")
            mPlaybackState.setState(PlaybackStateCompat.STATE_BUFFERING, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, PLAYBACK_SPEED_PAUSED)
            mPlaybackState.setActions(stopState.configSeekState())
            publishPlaybackState()

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
            mMediaSession.value.setMetadata(item.metadata())
            mNotification?.startNotification()
            log.d("Stream url was not null")
            mMediaPlayer.reset()
            tickerSub?.unsubscribe()
            mMediaPlayer.setDataSource(item.streamUrl)
            mMediaPlayer.setOnPreparedListener {
                log.d("Prepared, playing...")
                mIsPrepared = true
                play()
                tickerSub = Observable.interval(200, TimeUnit.MILLISECONDS)
                        .repeat()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { //Update playback state
                            if (mMediaPlayer.isPlaying) {
                                mPlaybackState.setState(PlaybackStateCompat.STATE_PLAYING, mMediaPlayer.currentPosition.toLong(), PLAYBACK_SPEED_PLAYING)
                                mPlaybackState.setActions(playState.configSeekState())
                            }

                        }
            }

            mMediaPlayer.setOnBufferingUpdateListener { mediaPlayer, progress ->
                if (!mIsPrepared) {
                    mPlaybackState.setState(PlaybackStateCompat.STATE_BUFFERING, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, PLAYBACK_SPEED_PAUSED)
                    mPlaybackState.setActions(stopState.configSeekState())
                    publishPlaybackState()
                }
            }
            mMediaPlayer.setOnCompletionListener {
                mPlaybackState.setState(PlaybackStateCompat.STATE_SKIPPING_TO_NEXT, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, PLAYBACK_SPEED_PAUSED)
                publishPlaybackState()
                tickerSub?.unsubscribe()
                next()
            }
            mMediaPlayer.setOnErrorListener { mediaPlayer, what, extra ->
                mPlaybackState.setState(PlaybackStateCompat.STATE_ERROR, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, PLAYBACK_SPEED_PAUSED)
                mPlaybackState.setActions(stopState.configSeekState())
                publishPlaybackState()
                tickerSub?.unsubscribe()
                log.e("Error in mediaPlayer $what-$extra")
                true
            }
            mMediaPlayer.prepareAsync()
        }
    }

    private fun PlaybackStateCompat.Builder.setActions(actions: Array<Long>) {
        log.d("Actions: ${Arrays.toString(actions)}")
        this.setActions(actions.bitwise())
    }

    override fun onDestroy() {
        super.onDestroy()
        mNotification?.stopNotification()
        tickerSub?.unsubscribe()
        stop()
        mMediaPlayer.release()
    }

    override fun onAudioFocusChange(focusChange: Int) {
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                play()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> pause()
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> mMediaPlayer.setVolume(0.5f, 0.5f)
            AudioManager.AUDIOFOCUS_LOSS -> stop()
        }
    }

    private fun SongItem.metadata() =
            MediaMetadataCompat.Builder()
                    .putString(MediaMetadataCompat.METADATA_KEY_TITLE, songTitle)
                    .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
                    .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, songTitle)
                    .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, artist)
                    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, length)
                    .putString(MediaMetadataCompat.METADATA_KEY_ART_URI, image)
                    .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, image)
                    .build()


    /*
    * Util with functions
    * */

    private fun publishPlaybackState() {
        val state = mPlaybackState.build()
        mMediaSession.value.setPlaybackState(state)
        //log.d("mPlaybackState: $state")
    }

    private val playState = arrayOf(
            PlaybackStateCompat.ACTION_PLAY_PAUSE,
            PlaybackStateCompat.ACTION_SEEK_TO,
            PlaybackStateCompat.ACTION_STOP)

    public fun play() {
        if(mMediaPlayer.isPlaying) return
        registerReceiver(mNoisyReciever, mNoisyFilter)
        val am = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val result = am.requestAudioFocus(this,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN)
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mMediaPlayer.setVolume(1.0f, 1.0f)
            mMediaPlayer.start()
            mMediaSession.value.isActive = true

            mPlaybackState.setState(PlaybackStateCompat.STATE_PLAYING, mMediaPlayer.currentPosition.toLong(), PLAYBACK_SPEED_PLAYING)
            mPlaybackState.setActions(playState.configSeekState())
            publishPlaybackState()
        }
    }

    private val pauseState = arrayOf(
            PlaybackStateCompat.ACTION_PLAY_PAUSE,
            PlaybackStateCompat.ACTION_SEEK_TO,
            PlaybackStateCompat.ACTION_STOP
    )

    public fun pause() {
        if(!mMediaPlayer.isPlaying) return
        unregisterReceiver(mNoisyReciever)
        if (mMediaPlayer.isPlaying) {
            mMediaPlayer.pause()
        }
        mPlaybackState.setState(PlaybackStateCompat.STATE_PAUSED, mMediaPlayer.currentPosition.toLong(), PLAYBACK_SPEED_PAUSED)
        mPlaybackState.setActions(pauseState.configSeekState())
        publishPlaybackState()
    }

    private val stopState = arrayOf(
            PlaybackStateCompat.ACTION_PLAY_PAUSE
    )

    public fun stop() {
        mPlaybackState.setState(PlaybackStateCompat.STATE_STOPPED, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, PLAYBACK_SPEED_PAUSED)
        mPlaybackState.setActions(stopState.configSeekState())
        publishPlaybackState()
        (getSystemService(Context.AUDIO_SERVICE) as AudioManager).abandonAudioFocus(this)
        mMediaPlayer.stop()
        mMediaSession.value.isActive = false
        mIsPrepared = false
    }

    private val skippingState = arrayOf(
            PlaybackStateCompat.ACTION_STOP
    )

    public fun next() {
        mIndex += 1
        if (mIndex < mSongs.size) {
            playSong(mIndex)
            mPlaybackState.setState(PlaybackStateCompat.STATE_SKIPPING_TO_NEXT, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, PLAYBACK_SPEED_PAUSED)
            mPlaybackState.setActions(skippingState.configSeekState())
            publishPlaybackState()
        }
    }

    public fun prev() {
        mIndex -= 1
        if (mIndex >= 0 && mIndex < mSongs.size) {
            playSong(mIndex)
            mPlaybackState.setState(PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, PLAYBACK_SPEED_PAUSED)
            mPlaybackState.setActions(skippingState.configSeekState())
            publishPlaybackState()
        }
    }

    public fun seekTo(pos: Long) {
        //Should update playing/buffering listeners
        mMediaPlayer.seekTo(pos.toInt())
    }

    public fun isPlaying(mSongItem: SongItem): Boolean = mSongItem.id == mCurrentSong?.id

    private fun Array<Long>.bitwise(): Long {
        if (size > 0) {
            return fold(this[0], { long1, long2 -> long1.or(long2) })
        } else {
            return 0
        }
    }

    private fun Array<Long>.configSeekState(): Array<Long> {
        val next = if (mIndex < mSongs.size - 1) PlaybackStateCompat.ACTION_SKIP_TO_NEXT else -1
        val prev = if (mIndex > 0) PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS else -1
        if (next != -1.toLong() && prev != -1.toLong()) {
            return this.plus(arrayOf(next, prev))
        } else if (next != -1.toLong()) {
            return this.plus(next)
        } else if (prev != -1.toLong()) {
            return this.plus(prev)
        } else {
            return this
        }
    }

    public fun getSessionToken(): MediaSessionCompat.Token {
        return mMediaSession.value.sessionToken
    }

    companion object IntentFactory {
        public fun getIntent(context: Context): Intent = Intent(context, MusicService::class.java)
    }

    private val callbacks = object : MediaSessionCompat.Callback() {
        override fun onPlay() {
            log.d("Play called")
            play()
        }

        override fun onStop() {
            log.d("Stop called")
            stop()
        }

        override fun onSkipToNext() {
            log.d("Skip to next called")
            next()
        }

        override fun onSeekTo(pos: Long) {
            log.d("Seek To called, pos: $pos")
            seekTo(pos)
        }

        override fun onSkipToPrevious() {
            log.d("Previous called")
            prev()
        }

    }
}