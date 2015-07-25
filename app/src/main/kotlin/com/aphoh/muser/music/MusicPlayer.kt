package com.aphoh.muser.music

import com.aphoh.muser.data.db.model.SongItem

/**
 * Created by Will on 7/25/15.
 */
public interface MusicPlayer{
    fun setQue(songs : List<SongItem>)
    fun setSongChangedListener(func : (SongItem, SongItem) -> Unit)
    fun getCurrentSong() : SongItem
}