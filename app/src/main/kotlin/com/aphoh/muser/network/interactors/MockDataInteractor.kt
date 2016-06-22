package com.aphoh.muser.network.interactors

import com.aphoh.muser.data.db.model.SongItem
import com.aphoh.muser.network.SortingConfig
import rx.Observable
import java.util.*
import java.util.concurrent.ThreadLocalRandom

/**
 * Created by Will on 1/4/16.
 */

class MockDataInteractor : DataInteractor {

    override fun refresh(subreddit: String, sortingConfig: SortingConfig): Observable<ArrayList<SongItem>> {
        return Observable.defer {
            Observable.just(IntRange(0, 40)
                    .map { mockItem() }
                    .toCollection(ArrayList<SongItem>()))
        }
    }

    override fun requestUrlForSongItem(songItem: SongItem): Observable<SongItem> {
        return Observable.just(songItem)
                .map {
                    it.length = 267000
                    it.streamUrl = "https://f537bc640baa6c0c16b9c8699e36ca9adfbfe2ce.googledrive.com/host/0B4NLnNU5sKUJTVluQmFVektCeFk"
                    it
                }
    }

    private companion object {
        fun randString() = UUID.randomUUID().toString()
        fun randInt() = ThreadLocalRandom.current().nextInt()
        fun randScore() = ThreadLocalRandom.current().nextInt(0, 3000)
        fun mockItem(): SongItem {
            val score = randScore()
            return SongItem(randString(),
                    "https://www.noao.edu/image_gallery/images/d3/ngc3582-500.jpg",
                    "https://www.noao.edu/image_gallery/images/d3/ngc3582-500.jpg",
                    "",
                    "",
                    "",
                    "",
                    score,
                    -1,
                    "mockArtist",
                    "mockSong")
        }
    }

}
