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
import java.util.*


/**
 * Created by Will on 7/5/2015.
 */
public class MuserDataInteractor(var okClient: OkHttpClient, val soundcloudKeys: SoundcloudKeys) : DataInteractor {

    var log = LogUtil(MuserDataInteractor::class.java.simpleName)

    var redditService: RedditService = RestAdapter.Builder()
            .setEndpoint("http://www.reddit.com")
            .setClient(OkClient(okClient))
            .build().create(RedditService::class.java)

    var soundcloudService: SoundcloudService = RestAdapter.Builder()
            .setEndpoint("http://api.soundcloud.com")
            .setClient(OkClient(okClient))
            .build().create(SoundcloudService::class.java)

    // ===========================================
    // Network Calls
    // ===========================================


    public override fun refresh(subreddit: String): Observable<ArrayList<SongItem>> {
        return redditService.getSubredditSubmissions(subreddit, 100)
                .map { it.data.postItems }
                .map { items ->
                    var subs = Select().from(Subreddit::class.java).where(Condition.column(DBTableAlias.SubredditNAME).`is`(subreddit)).queryList()
                    var sub = Subreddit()

                    if (subs.isEmpty() && items.isNotEmpty()) {
                        sub.name = subreddit
                        saveSub(sub)
                    } else if (items.isNotEmpty() && subs.size > 0) {
                        sub = subs.get(0)
                    }

                    var result =
                            items.map { it.data }
                                    .filter { isSoundcloudUrl(it.url) }
                                    .filter { it.media != null}
                                    .filter { it.media.oembed != null }
                                    .map { SongItem.fromPostData(it) }
                                    .map {
                                        it.associateSubreddit(sub)
                                        it
                                    }
                                    .toArrayList()


                    clearSongs(sub)
                    saveItems(result)
                    result
                }
    }

    public override fun requestUrlForSongItem(songItem: SongItem): Observable<SongItem> {
        return soundcloudService.getSongFromUrl(songItem.linkUrl, soundcloudKeys.clientId)
                .map({ track ->
                    log.d("Track url returned: ${track.stream_url}")
                    var stream = "${track.stream_url}?client_id=${soundcloudKeys.clientId}"
                    songItem.streamUrl = stream
                    songItem.waveformUrl = track.waveform_url
                    songItem.update()
                    songItem
                })
    }

    // ===========================================
    // DB Utility Functions
    // ===========================================

    private fun clearSongs(sub: Subreddit) {
        var songs: List<BaseModel> = Select()
                .from(SongItem::class.java)
                .where(Condition.column(DBTableAlias.SubredditID).`is`(sub.id))
                .queryList()
        songs.forEach { it.delete() }
    }

    private fun saveSub(item: Subreddit) {
        item.save()
    }

    private fun saveItems(items: ArrayList<SongItem>) {
        items.forEach { it.save() }
    }


    private fun isSoundcloudUrl(url: String): Boolean {
        return url.startsWith("http://soundcloud.com") || url.startsWith("https://soundcloud.com")
    }

    //Observable Getters

    public override fun getSongItem(songId: String): Observable<List<SongItem>> = Observable.just(
            Select()
                    .from(SongItem::class.java)
                    .where(Condition.column(DBTableAlias.SongItemID).`is`(songId))
                    .queryList()
    )


    public override fun getSongItems(): Observable<List<SongItem>> = Observable.just(
            Select()
                    .from(SongItem::class.java)
                    .queryList()
    )

    public override fun getSubreddits(): Observable<List<Subreddit>> = Observable.just(
            Select()
                    .from(Subreddit::class.java)
                    .queryList())

    public override fun getSongItemsForSubreddit(subredditId: Int): Observable<List<SongItem>> = Observable.just(
            Select()
                    .from(SongItem::class.java)
                    .where(Condition.column(DBTableAlias.SongItemSUBREDDITMODELCONTAINER_SUBREDDIT_ID).`is`(subredditId))
                    .queryList())

    companion object Utils {
        public fun removeByLine(s: String): String = s.substringBeforeLast(" by")
    }
}
