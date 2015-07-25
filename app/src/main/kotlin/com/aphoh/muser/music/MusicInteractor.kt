package com.aphoh.muser.music

import com.aphoh.muser.App
import com.aphoh.muser.base.BaseApplication
import com.aphoh.muser.data.db.model.SongItem

/**
 * Created by Will on 7/25/15.
 */
public class MusicInteractor(){

    var mSongs : List<SongItem>? = null;
    var mCurrentIndex : Int? = null

    var mMusicPlayer : MusicPlayer? = null

    public fun setSongItems(songs : List<SongItem>){
        mSongs = songs
        mCurrentIndex = 0
        playSong(0)
    }


    public fun playSong(index : Int){
        mMusicPlayer.playSong(mSongs.get(index))
    }

}
