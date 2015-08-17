package com.aphoh.muser.music

import com.aphoh.muser.data.db.model.SongItem
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.ArrayList
import java.util.NoSuchElementException

/**
 * Created by Will on 7/25/15.
 */
public class MusicInteractor(var mMusicPlayer: MusicPlayer) {

    var mSongChangedListener: ((prev: SongItem, next: SongItem) -> Unit)? = null
    var mErrorListener: (Throwable) -> Unit = {}
    var mIsPlaying = false
    var mIsFinished = false

    var mSongs: ArrayList<SongItem> = ArrayList()
        set(value) {
            $mSongs = value
            mCurrentIndex = 0
        }
    public var mCurrentIndex: Int = 0

    init {
        mMusicPlayer.addSongFinishedListener { finishedSong ->
            next()
        }
    }

    public fun playFromStart() {
        if (mSongs.size() > 0) {
            playSong(mSongs.get(0))
            mCurrentIndex = 0
            mMusicPlayer.addSongFinishedListener { song ->
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

    public fun pause() {
        mMusicPlayer.pause()
        mIsPlaying = false
    }

    public fun destroy() {
        mMusicPlayer.destroy()
        mIsPlaying = false
        mIsFinished = true
    }

    public fun resume() {
        mMusicPlayer.resume()
        mIsPlaying = true
    }

    public fun next(){
        mCurrentIndex += 1
        if(mCurrentIndex < mSongs.size()){
            playSong(mSongs.get(mCurrentIndex))
        } else {
           finish()
        }
    }

    private fun finish(){
        mIsFinished = true
        mIsPlaying = false
        mMusicPlayer.stop()
    }

    /* This clears all current songs in the que */
    public fun clearSongItems() {
        mSongs = ArrayList<SongItem>()
    }

    public fun getCurrentSong(): SongItem? {
        if(mSongs.isEmpty() || mCurrentIndex >= mSongs.size()) return null
        return mSongs.get(mCurrentIndex)
    }

    public fun playSong(songItem: SongItem) {
        mMusicPlayer.playSong(songItem)
        mIsPlaying = true
    }


}
