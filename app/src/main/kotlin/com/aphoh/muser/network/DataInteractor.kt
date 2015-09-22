package com.aphoh.muser.network

import com.aphoh.muser.data.db.model.SongItem
import com.aphoh.muser.data.db.model.Subreddit
import rx.Observable
import java.util.*

/**
 * Created by Will on 7/5/2015.
 */
interface DataInteractor {

    // ===========================================
    // Network Calls
    // ===========================================

    public fun refresh(subreddit: String): Observable<ArrayList<SongItem>>

    public fun requestUrlForSongItem(songItem: SongItem): Observable<SongItem>

    // ===========================================
    // DB Utility Functions
    // ===========================================

    protected fun isSoundcloudUrl(url: String): Boolean

    //Observable Getters

    public fun getSongItem(songId: String): Observable<List<SongItem>>

    public fun getSongItems(): Observable<List<SongItem>>

    public fun getSubreddits(): Observable<List<Subreddit>>

    public fun getSongItemsForSubreddit(subredditId: Int): Observable<List<SongItem>>
}

public interface SongResponseModel {
    var id: String;
    var image: String
    var thumbnail: String
    var linkUrl: String
    var streamUrl: String
    var waveformUrl: String
    var score: Int
    var artist: String
    var songTitle: String
}
