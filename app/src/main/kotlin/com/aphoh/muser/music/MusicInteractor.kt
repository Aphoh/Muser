package com.aphoh.muser.music

import com.aphoh.muser.App
import com.aphoh.muser.data.db.model.SongItem
import com.aphoh.muser.network.DataInteractor
import java.util.*
import javax.inject.Inject

/**
 * Created by Will on 7/25/15.
 */
public class MusicInteractor(var mMusicPlayer : MusicPlayer) {
    var mInteractor: DataInteractor? = null
            @Inject set
    init{
        App.applicationComponent.injectMusicInteractor(this)
    }

    private var mSongs: ArrayList<SongItem> = ArrayList<SongItem>()
            set(value){
                mSongs = value
                mCurrentIndex = 0
            }
    public var mCurrentIndex: Int? = null

    public var mMusicPlayer: MusicPlayer? = null
            set(value){
                mMusicPlayer = value
                clearSongItems()

            }

    /* This clears all current songs in the que */
    public fun clearSongItems(){
        mSongs = ArrayList<SongItem>()
    }
    /*
    * Notifies the MusicPlayer to play a song at the given index
    * @param index The index to play the song at
    * */
    public fun playSong(index: Int) {
        mMusicPlayer.playSong(mSongs.get(index))
    }



}
