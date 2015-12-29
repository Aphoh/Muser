package com.aphoh.muser.music

/**
 * Created by Will on 9/28/15.
 */
public interface MusicView {
    var id: Int
    fun publishAlbumArt(albumArt: String)
    fun publishSongName(songName: String)
    fun publishSongArtist(songArtist: String?)
    fun publishProgress(seconds: Int)
    fun publishError(error: String)
}