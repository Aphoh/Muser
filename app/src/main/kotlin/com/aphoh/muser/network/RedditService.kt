package com.aphoh.muser.network

import com.aphoh.muser.data.network.model.reddit.Listings
import retrofit.http.GET
import retrofit.http.Path
import retrofit.http.Query
import rx.Observable

/**
 * Created by Will on 7/1/2015.
 */
interface RedditService {
    GET("/r/{subreddit}/top.json?t=week")
    fun getSubredditSubmissions(Path("subreddit") subreddit: String, Query("limit") limit : Int): Observable<Listings>;
}