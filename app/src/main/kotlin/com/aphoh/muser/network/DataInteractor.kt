package com.aphoh.muser.network

import com.aphoh.muser.data.db.model.SongItem
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

    //Observable Getters

}
