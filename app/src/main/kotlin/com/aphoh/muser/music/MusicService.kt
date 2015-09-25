package com.aphoh.muser.music

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.IBinder
import android.os.PowerManager
import com.aphoh.muser.data.db.model.SongItem

/**
 * Created by Will on 7/12/15.
 */
public class MusicService() : Service(), MusicPlayer {

    override var isPlaying: Boolean
        get() = mMediaPlayer.isPlaying
        set(value) {
            throw UnsupportedOperationException("Cannot set isPlaying, value: $value")
        }
    var mSong: SongItem? = null
    var mSongFinishedListener: (SongItem) -> Unit = {}
    var mMusicInteractor: MusicInteractor = MusicInteractor(this)

    var mBinder = MusicBinder(mMusicInteractor)

    private var mMediaPlayer = MediaPlayer()

    override fun onCreate() {
        super.onCreate()
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mMediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK)
    }

    override fun onDestroy() {
        super.onDestroy()
        destroy()
    }

    // ===========================================================
    // MusicPlayer Interface methods
    // ===========================================================

    override fun pause() {
        mMediaPlayer.pause()
    }

    override fun resume() {
        mMediaPlayer.start()
    }

    override fun stop() {
        mMediaPlayer.stop()
    }

    override fun destroy() {
        mMediaPlayer.release()
    }

    override fun addSongFinishedListener(func: (SongItem?) -> Unit) {
        mSongFinishedListener = func
    }

    override fun getCurrentSong(): SongItem? {
        return mSong
    }

    override fun playSong(song: SongItem) {
        mSong = song
        mMediaPlayer.setDataSource(song.streamUrl)
        mMediaPlayer.prepare()
        mMediaPlayer.start()
        mMediaPlayer.setOnCompletionListener { mSongFinishedListener.invoke(song) }
    }


    override fun onBind(intent: Intent): IBinder? = mBinder

    public inner class MusicBinder(var musicInteractor: MusicInteractor) : android.os.Binder() {
        public fun getInteractor(): MusicInteractor{
            return musicInteractor
        }
    }
}