package com.aphoh.muser.music

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.aphoh.muser.App
import com.aphoh.muser.data.db.model.SongItem
import javax.inject.Inject

/**
 * Created by Will on 7/12/15.
 */
public class MusicService() : Service(), MusicPlayer{

    var mSongs: List<SongItem>? = null
    var mSongChangedListener : ((SongItem, SongItem) -> Unit)? = null
    var mMusicInteractor : MusicInteractor = MusicInteractor(this)
    private var binder = MusicBinder()

    override fun onCreate() {
        super<Service>.onCreate()
    }

    // ===========================================================
    // MusicPlayer Interface methods
    // ===========================================================

    override fun pause() {
        throw UnsupportedOperationException()
    }

    override fun resume() {
        throw UnsupportedOperationException()
    }

    override fun stop() {
        throw UnsupportedOperationException()
    }

    override fun destroy() {
        throw UnsupportedOperationException()
    }

    override fun addSongFinishedListener(func: (SongItem) -> Unit) {
        throw UnsupportedOperationException()
    }

    override fun getCurrentSong(): SongItem {
        throw UnsupportedOperationException()
    }

    override fun playSong(song: SongItem) {
        throw UnsupportedOperationException()
    }


    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException()
    }

    public inner class MusicBinder : android.os.Binder(){
        public fun getMusicInteractor(): MusicInteractor{
            return mMusicInteractor
        }
    }
}