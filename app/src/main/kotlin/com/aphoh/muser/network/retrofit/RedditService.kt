package com.aphoh.muser.network.retrofit

import com.aphoh.muser.data.network.model.reddit.Listings
import retrofit.http.GET
import retrofit.http.Path
import retrofit.http.Query
import rx.Observable

/**
 * Created by Will on 7/1/2015.
 */
interface RedditService {
    @GET("/r/{subreddit}/{category}.json")
    fun getSubredditSubmissions(@Path("subreddit") subreddit: String,
                                @Path("category") category: String,
                                @Query("t") time: String?,
                                @Query("limit") limit: Int): Observable<Listings>
}