package com.aphoh.muser.network

import retrofit.http.GET
import rx.Observable

/**
 * Created by Will on 7/1/2015.
 */
trait RedditService{
    GET("/r/{subreddit}/hot.json?sort")

}