package com.aphoh.muser.network

import android.content.Context
import com.aphoh.muser.data.db.model.DBTableAlias
import com.aphoh.muser.data.db.model.SongItem
import com.aphoh.muser.data.db.model.Subreddit
import com.raizlabs.android.dbflow.sql.builder.Condition
import com.raizlabs.android.dbflow.sql.language.Select
import retrofit.RestAdapter
import rx.Observable
import java.util.ArrayList

/**
 * Created by Will on 7/5/2015.
 */
public class DataInteractor(val context: Context) {

    private final val clientId = "81e4c3f234711ed893e32397d52cc2e6"

    var redditService: RedditService = RestAdapter.Builder()
            .setEndpoint("http://reddit.com")
            .build().create(javaClass<RedditService>())

    public fun refresh(subreddit: String): Observable<ArrayList<SongItem>> {
        return redditService.getSubredditSubmissions(subreddit)
                .map { it.getData().getPostItems() }
                .map { items ->
                    var newItems = ArrayList<SongItem>()
                    var sub = Subreddit()
                    sub.setName(subreddit)
                    for (item in items) {
                        val data = item.getData()
                        if (isSoundcloudUrl(data.getUrl())) {
                            var songItem = SongItem()
                            songItem.setImage(data.getMedia().getOembed().getThumbnail_url())
                            songItem.setImage(songItem.getThumbnail())
                            songItem.setUrl(data.getUrl())
                            songItem.setScore(data.getScore())
                            songItem.associateSubreddit(sub)
                            newItems.add(songItem)
                        }
                    }
                    saveItems(newItems, sub)
                    newItems
                }
    }

    private fun saveItems(items: ArrayList<SongItem>, subreddit: Subreddit) {
        subreddit.save()
        for (item in items) {
            item.save()
        }
    }

    private fun isSoundcloudUrl(url: String): Boolean {
        return url.contains("soundcloud.com")
    }

    //Observable Getters

    public fun getSubreddits() : Observable<List<Subreddit>> = Observable.just(
            Select()
            .from(javaClass<Subreddit>())
            .queryList())

    public fun getSongItemsForSubreddit(subredditId : Int) : Observable<List<SongItem>> = Observable.just(
            Select()
                    .from(javaClass<SongItem>())
            .where(Condition.column(DBTableAlias.SongItemSUBREDDITMODELCONTAINER_SUBREDDIT_ID).`is`(subredditId))
            .queryList())
}
