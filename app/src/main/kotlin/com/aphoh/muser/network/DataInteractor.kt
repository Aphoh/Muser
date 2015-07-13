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
public class DataInteractor(var okClient: OkHttpClient) {

    private final val clientId = "81e4c3f234711ed893e32397d52cc2e6"

    var log = LogUtil(javaClass<DataInteractor>().getSimpleName())

    var redditService: RedditService = RestAdapter.Builder()
            .setEndpoint("http://www.reddit.com")
            .setClient(OkClient(okClient))
            .build().create(javaClass<RedditService>())

    var soundcloudService: SoundcloudService = RestAdapter.Builder()
            .setEndpoint("http://api.soundcloud.com")
            .setClient(OkClient(okClient))
            .build().create(javaClass<SoundcloudService>())

    // ===========================================
    // Network Calls
    // ===========================================

    public fun refresh(subreddit: String): Observable<ArrayList<SongItem>> {
        return redditService.getSubredditSubmissions(subreddit, 100)
                .map { it.getData().getPostItems() }
                .map { items ->
                    var newItems = ArrayList<SongItem>()
                    var subs = Select().from(javaClass<Subreddit>()).where(Condition.column(DBTableAlias.SubredditNAME).`is`(subreddit)).queryList()
                    var sub = Subreddit()
                    if (subs.size() == 0) {
                        sub.setName(subreddit)
                        saveSub(sub)
                    } else {
                        sub = subs.get(0)
                    }
                    for (item in items) {
                        val data = item.getData()
                        if (isSoundcloudUrl(data.getUrl())) {
                            var songItem = SongItem()
                            var oembed = data.getMedia().getOembed()
                            songItem.setId(data.getId())
                            songItem.setImage(oembed.getThumbnail_url())
                            songItem.setArtist(oembed.getAuthor_name())
                            songItem.setSongTitle(removeByLine(oembed.getTitle()))
                            songItem.setUrl(data.getUrl())
                            songItem.setScore(data.getScore())
                            songItem.associateSubreddit(sub)
                            newItems.add(songItem)
                        }
                    }
                    clearSongs(sub)
                    saveItems(newItems)
                    newItems
                }
    }

    public fun requestUrlForSongItem(songItem: SongItem): Observable<SongItem> {
        return soundcloudService.getSongFromUrl(songItem.getUrl(), clientId)
                .map({track ->
                    log.d("Track url returned: ${track.getStream_url()}")
                    songItem.setStreamUrl(track.getStream_url())
                    songItem.setWaveformUrl(track.getWaveform_url())
                    songItem.update()
                    songItem
                })
    }

    // ===========================================
    // DB Utility Functions
    // ===========================================

    private fun clearSongs(sub: Subreddit) {
        var songs: List<BaseModel> = Select().from(javaClass<SongItem>()).queryList()
        sub.delete()
        for (song in songs) {
            song.delete()
        }
    }

    private fun saveSub(item: Subreddit) {
        item.save()
    }

    private fun saveItems(items: ArrayList<SongItem>) {
        for (item in items) {
            item.save()
        }
    }

    private fun removeByLine(s: String): String = s.substringBeforeLast(" by")

    private fun isSoundcloudUrl(url: String): Boolean {
        return url.contains("soundcloud.com")
    }

    //Observable Getters

    public fun getSongItems(): Observable<List<SongItem>> = Observable.just(
            Select()
                    .from(javaClass<SongItem>())
                    .queryList()
    )

    public fun getSubreddits(): Observable<List<Subreddit>> = Observable.just(
            Select()
                    .from(javaClass<Subreddit>())
                    .queryList())

    public fun getSongItemsForSubreddit(subredditId: Int): Observable<List<SongItem>> = Observable.just(
            Select()
                    .from(javaClass<SongItem>())
                    .where(Condition.column(DBTableAlias.SongItemSUBREDDITMODELCONTAINER_SUBREDDIT_ID).`is`(subredditId))
                    .queryList())
}
