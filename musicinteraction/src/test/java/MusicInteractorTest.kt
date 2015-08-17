package com.aphoh.musicinteraction.test

import com.musicinteraction.MusicInteractor
import com.musicinteraction.MusicPlayer
import com.musicinteraction.model.RequestModel
import org.junit.Before
import org.junit.Test
import java.util.ArrayList
import kotlin.test.*

/**
 * Created by Will on 7/25/15.
 */

public class MusicInteractorTest() {
    var musicPlayer: MockMusicPlayer = MockMusicPlayer()
    var musicInteractor: MusicInteractor = MusicInteractor(musicPlayer)
    var mSongs = ArrayList<RequestModel>()

    Before
    public fun setup(){
        mSongs.add(RequestModel.Factory.createBlankModel())
        mSongs.get(0).id = "1"
        mSongs.add(RequestModel.Factory.createBlankModel())
        mSongs.get(1).id = "2"
        musicInteractor.mSongs = mSongs
    }

    Test
    public fun testSettingSongs() {
        musicInteractor.playSongs()

        assertEquals(mSongs.get(0), musicPlayer.getCurrentSong())
        assertTrue(musicInteractor.mIsPlaying)
        assertTrue(musicPlayer.isPlaying)
    }

    Test
    public fun testPausePlayDestroy(){
        musicInteractor.playSongs()
        assertTrue(musicInteractor.mIsPlaying)
        assertTrue(musicPlayer.isPlaying)

        musicInteractor.pause()
        assertFalse(musicInteractor.mIsPlaying)
        assertFalse(musicPlayer.isPlaying)

        musicInteractor.resume()
        assertTrue(musicInteractor.mIsPlaying)
        assertTrue(musicPlayer.isPlaying)

        musicInteractor.destroy()
        assertTrue(musicPlayer.isDestroyed)
        assertFalse(musicPlayer.isPlaying)
    }

    Test
    public fun testClearSongs(){
        musicInteractor.clearRequestModels()
        assertTrue(musicInteractor.mSongs.isEmpty())
        assertFalse(musicInteractor.mIsPlaying)
        assertNull(musicPlayer.getCurrentSong())
        assertFalse(musicPlayer.isPlaying)
    }

    Test
    public fun testOnSongFinishPlayNext(){
        assertNotNull(musicPlayer.listener)
        musicPlayer.listener!!.invoke(musicPlayer.getCurrentSong()!!)

        assertEquals(musicInteractor.getCurrentSong(), mSongs.get(1))
        assertEquals(musicPlayer.getCurrentSong(), mSongs.get(1))

    }

    private inner class MockMusicPlayer : MusicPlayer {

        var listener : ((RequestModel) -> Unit)? = null
        var playingSong : RequestModel? = null

        var isDestroyed = false
        var isPlaying = false

        override fun playSong(song: RequestModel) {
            playingSong = song
            isPlaying = true
        }

        override fun pause() {
            isPlaying = false
        }

        override fun resume() {
            isPlaying = true
        }

        override fun stop() {
            isPlaying = false
            playingSong = null
        }

        override fun destroy() {
            isPlaying = false
            isDestroyed = true
        }

        override fun addSongFinishedListener(func: (RequestModel) -> Unit) {
            listener = func
        }

        override fun getCurrentSong(): RequestModel? {
            return playingSong
        }
    }
}
