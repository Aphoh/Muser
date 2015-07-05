package com.aphoh.muser.network

import com.aphoh.muser.data.network.model.Listings
import retrofit.http.GET
import retrofit.http.Path
import rx.Observable

/**
 * Created by Will on 7/1/2015.
 */
trait RedditService{
    GET("/r/{subreddit}/top.json?t=week")
    fun getSubredditSubmissions(Path("subreddit") subreddit : String) : Observable<Listings>;
}