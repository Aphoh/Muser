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

    init{
        App.applicationComponent.injectMusicInteractor(this)
    }

    var mInteractor: DataInteractor? = null
            @Inject set
    var isPlaying = false

    private var mSongs: ArrayList<SongItem> = ArrayList<>()
            set(value){
                mSongs = value
                mCurrentIndex = 0
            }
    public var mCurrentIndex: Int = 0

    public fun playSongs(){
        if(mSongs.size() > 0) {
            mMusicPlayer.playSong(mSongs.get(0))
            mMusicPlayer.setSongFinishedListener { song ->
                var next = mSongs.indexOf(song) + 1
                if (next < mSongs.size()) {
                    mCurrentIndex = next
                    mMusicPlayer.playSong(mSongs.get(next))
                }
            }
        } else {
            throw NoSuchElementException("There are no songs set, the service cannot play")
        }
    }

    public fun pause(){
        mMusicPlayer.pause()
    }

    /* This clears all current songs in the que */
    public fun clearSongItems(){
        mSongs = ArrayList<SongItem>()
    }

    public fun getCurrentSong(): SongItem{
        return mSongs.get(mCurrentIndex)
    }
    /*
    * Notifies the MusicPlayer to play a song at the given index
    * @param index The index to play the song at
    * */
    public fun playSong(index: Int) {
        mMusicPlayer.playSong(mSongs.get(index))
    }



}
