package com.aphoh.muser.music

import com.aphoh.muser.data.db.model.SongItem

/**
 * Created by Will on 7/25/15.
 */
public interface MusicPlayer{
    fun playSong(song : SongItem)
    fun pause()
    fun resume()
    fun stop()
    fun destroy()
    fun addSongFinishedListener(func : (finishedSong: SongItem) -> Unit)
    fun getCurrentSong() : SongItem
}
