package com.aphoh.muser.network

import com.aphoh.muser.data.network.model.soundcloud.Track
import retrofit.http.GET
import retrofit.http.Query
import rx.Observable

/**
 * Created by Will on 7/5/2015.
 */
interface SoundCloudService {
    GET("/resolve.json")
    fun getSongFromUrl(Query("url") url: String, Query("client_id") clientId: String): Observable<Track>
}