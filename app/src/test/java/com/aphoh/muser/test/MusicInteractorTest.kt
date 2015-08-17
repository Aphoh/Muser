package com.aphoh.muser.test

import com.aphoh.muser.data.db.model.SongItem
import com.aphoh.muser.data.db.model.SongItemBuilder
import com.aphoh.muser.music.MusicInteractor
import com.aphoh.muser.music.MusicPlayer
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
    var mSongs = ArrayList<SongItem>()

    init{
        mSongs.add(SongItemBuilder()
                .setId(1.toString()).createSongItem())
        mSongs.add(SongItemBuilder()
                .setId(1.toString()).createSongItem())
    }

    Before
    public fun setup(){
        musicPlayer = MockMusicPlayer()
        musicInteractor = MusicInteractor(musicPlayer)
        musicInteractor.mSongs = mSongs
        musicInteractor.mCurrentIndex = 0
    }

    Test
    public fun testSettingSongs() {
        musicInteractor.playFromStart()

        assertEquals(mSongs.get(0), musicPlayer.getCurrentSong())
        assertTrue(musicInteractor.mIsPlaying)
        assertTrue(musicPlayer.isPlaying)
    }

    Test
    public fun testPausePlayDestroy(){
        musicInteractor.playFromStart()
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
        musicInteractor.clearSongItems()
        assertTrue(musicInteractor.mSongs.isEmpty())
        assertFalse(musicInteractor.mIsPlaying)
        assertNull(musicPlayer.getCurrentSong())
        assertNull(musicInteractor.getCurrentSong())
        assertFalse(musicInteractor.mIsPlaying)
        assertFalse(musicPlayer.isPlaying)
    }

    Test
    public fun testOnSongFinishPlayNext(){
        musicInteractor.playFromStart()
        assertNotNull(musicPlayer.listener)
        assertNotNull(musicPlayer.getCurrentSong())

        musicPlayer.listener!!.invoke(musicPlayer.getCurrentSong()!!)
        assertEquals(1, musicInteractor.mCurrentIndex)
        assertEquals(mSongs.get(1), musicInteractor.getCurrentSong())
        assertEquals(mSongs.get(1), musicPlayer.getCurrentSong())
    }

    Test
    public fun testNext(){
        musicInteractor.playFromStart()
        musicInteractor.next()

        assertEquals(musicInteractor.mCurrentIndex, 1)
        assertEquals(musicInteractor.mSongs.size(), 2)
        assertEquals(mSongs.get(1), musicInteractor.getCurrentSong())
        assertTrue(musicInteractor.mIsPlaying)
    }

    Test
    public fun testFinishingViaNext(){
        musicInteractor.playFromStart()
        musicInteractor.next()
        musicInteractor.next()

        assertTrue(musicInteractor.mIsFinished)
        assertFalse(musicInteractor.mIsPlaying)
        assertFalse(musicPlayer.isPlaying)
        assertNull(musicInteractor.getCurrentSong())
        assertNull(musicPlayer.getCurrentSong())
    }

    private inner class MockMusicPlayer : MusicPlayer {

        var listener : ((SongItem) -> Unit)? = null
        var playingSong : SongItem? = null

        var isDestroyed = false
        var isPlaying = false

        override fun playSong(song: SongItem) {
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

        override fun addSongFinishedListener(func: (SongItem) -> Unit) {
            listener = func
        }

        override fun getCurrentSong(): SongItem? {
            return playingSong
        }
    }
}
