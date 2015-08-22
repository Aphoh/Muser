package com.aphoh.muser.network

import com.aphoh.muser.data.db.model.DBTableAlias
import com.aphoh.muser.data.db.model.SongItem
import com.aphoh.muser.data.db.model.Subreddit
import com.aphoh.muser.util.LogUtil
import com.raizlabs.android.dbflow.sql.builder.Condition
import com.raizlabs.android.dbflow.sql.language.Select
import com.raizlabs.android.dbflow.structure.BaseModel
import com.squareup.okhttp.OkHttpClient
import retrofit.RestAdapter
import retrofit.client.OkClient
import rx.Observable
import java.util.ArrayList

/**
 * Created by Will on 7/5/2015.
 */
interface DataInteractor{

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

public interface SongRequestModel{
    public fun getId() : String
    public fun getImage() : String
    public fun getThumbnail() : String
    public fun getLinkUrl() : String
    public fun getStreamUrl() : String
    public fun getWaveFormUrl() : String
    public fun getScore() : Int
    public fun getArtist() : String
    public fun getSongTitle() : String
}
