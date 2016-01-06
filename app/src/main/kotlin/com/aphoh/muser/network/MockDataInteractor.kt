package com.aphoh.muser.network

import com.aphoh.muser.data.db.model.SongItem
import rx.Observable
import java.util.*
import java.util.concurrent.ThreadLocalRandom

/**
 * Created by Will on 1/4/16.
 */

public class MockDataInteractor : DataInteractor {

    override fun refresh(subreddit: String): Observable<ArrayList<SongItem>> {
        return Observable.defer {
            Observable.just(IntRange(0, 40)
                    .map { mockItem() }
                    .toArrayList())
        }
    }

    override fun requestUrlForSongItem(songItem: SongItem): Observable<SongItem> {
        return Observable.just(songItem)
                .map {
                    it.length = 267000
                    it.linkUrl = "http://picosong.com/EPVR"
                    it
                }
    }

    private companion object {
        fun randString() = UUID.randomUUID().toString()
        fun randInt() = ThreadLocalRandom.current().nextInt()
        fun mockItem(): SongItem {
            return SongItem(randString(),
                    "",
                    "https://www.noao.edu/image_gallery/images/d3/ngc3582-500.jpg",
                    "",
                    "",
                    "",
                    randInt(),
                    -1,
                    "mockArtist",
                    "mockSong")
        }
    }

}
