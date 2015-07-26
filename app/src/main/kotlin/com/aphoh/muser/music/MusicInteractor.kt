package com.aphoh.muser.music

import com.aphoh.muser.App
import com.aphoh.muser.data.db.model.SongItem
import com.aphoh.muser.network.DataInteractor
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.ArrayList
import java.util.NoSuchElementException
import javax.inject.Inject

/**
 * Created by Will on 7/25/15.
 */
public class MusicInteractor(var mMusicPlayer: MusicPlayer) {

    var mSongChangedListener: ((prev: SongItem, next: SongItem) -> Unit)? = null
    var mErrorListener: (Throwable) -> Unit = {}
    var mInteractor: DataInteractor? = null
        @Inject set
    var mIsPlaying = false

    private var mSongs: ArrayList<SongItem> = ArrayList()
        set(value) {
            mSongs = value
            mCurrentIndex = 0
        }
    public var mCurrentIndex: Int = 0

    init {
        App.applicationComponent.injectMusicInteractor(this)
        mMusicPlayer.addSongFinishedListener { finishedSong ->
            var next = mSongs.indexOf(finishedSong) + 1
            if (next < mSongs.size()) {
                mSongChangedListener?.invoke(finishedSong, mSongs.get(next))
            }
        }
    }

    public fun playSongs() {
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

    public fun resume(){
        mMusicPlayer.resume()
        mIsPlaying = true
    }

    /* This clears all current songs in the que */
    public fun clearSongItems() {
        mSongs = ArrayList<SongItem>()
    }

    public fun getCurrentSong(): SongItem {
        return mSongs.get(mCurrentIndex)
    }

    /*
    * Notifies the MusicPlayer to play a song at the given index
    * @param index The index to play the song at
    * */
    public fun playSong(songItem: SongItem) {
        if (songItem.getStreamUrl() == null) {
            mInteractor.requestUrlForSongItem(songItem)
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        mMusicPlayer.playSong(it)
                        mIsPlaying = true
                    }, {
                        mErrorListener.invoke(it)
                    })
        } else {
            mMusicPlayer.playSong(songItem)
            mIsPlaying = true
        }
    }


}
