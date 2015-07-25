package com.aphoh.muser.music

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.aphoh.muser.data.db.model.SongItem

/**
 * Created by Will on 7/12/15.
 */
public class MusicService() : Service(), MusicPlayer{

    var mSongs: List<SongItem>? = null
    var mSongChangedListener : ((SongItem, SongItem) -> Unit)? = null

    override fun setQue(songs: List<SongItem>) {
        this.mSongs = songs
    }

    override fun setSongChangedListener(func: (SongItem, SongItem) -> Unit) {
        mSongChangedListener = func
    }

    override fun getCurrentSong(): SongItem {
        throw UnsupportedOperationException()
    }

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException()
    }
}