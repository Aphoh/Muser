package com.aphoh.muser.network.interactors

import com.aphoh.muser.data.db.model.SongItem
import com.aphoh.muser.network.SortingConfig
import rx.Observable
import java.util.*

/**
 * Created by Will on 7/5/2015.
 */
interface DataInteractor {

    // ===========================================
    // Network Calls
    // ===========================================

    public fun refresh(subreddit: String, sortingConfig: SortingConfig): Observable<ArrayList<SongItem>>

    public fun requestUrlForSongItem(songItem: SongItem): Observable<SongItem>

}
