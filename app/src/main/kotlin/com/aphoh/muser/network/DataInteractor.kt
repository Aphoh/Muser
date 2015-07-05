package com.aphoh.muser.network

import android.content.Context
import com.aphoh.muser.data.db.model.SongItem
import com.aphoh.muser.data.db.model.Subreddit
import com.aphoh.muser.data.network.model.Listings
import retrofit.RestAdapter
import rx.Observable
import rx.functions.Action1
import rx.functions.Func1
import rx.lang.kotlin.observable
import rx.lang.kotlin.subscriber
import java.util.*
import java.util.List

/**
 * Created by Will on 7/5/2015.
 */
public class DataInteractor(val context : Context){
    var redditService : RedditService = RestAdapter.Builder()
            .setEndpoint("http://reddit.com")
            .build().create(javaClass<RedditService>())

    public fun getSongItemsForSubreddit(subreddit : String) : Observable<ArrayList<SongItem>> {
        return redditService.getSubredditSubmissions(subreddit)
            .map{it.getData().getPostItems()}
            .map{items ->
                var newItems = ArrayList<SongItem>()
                for(item in items) {
                    val data = item.getData()
                    if (isSoundcloudUrl(data.getUrl())) {
                        var songItem = SongItem()
                        songItem.setImage(data.getMedia().getOembed().getThumbnail_url())
                        songItem.setImage(songItem.getThumbnail())
                        songItem.setUrl(data.getUrl())
                        songItem.setScore(data.getScore())
                        newItems.add(songItem)
                    }
                }
                newItems
            }
    }

    public fun refresh(subreddit : String): Observable<ArrayList<SongItem>>{
        return redditService.getSubredditSubmissions(subreddit)
                .map{it.getData().getPostItems()}
                .map{items ->
                    var newItems = ArrayList<SongItem>()
                    var sub = Subreddit()
                    sub.setName(subreddit)
                    for(item in items) {
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
                    newItems
                }
    }


    private fun saveItems(items : List<SongItem>, subreddit : Subreddit){
        for(item in items){
            if(!item.exists()) {
                item.save()
            }else{
                item.delete()
                item.save()
            }
        }
    }

    private fun isSoundcloudUrl(url : String) : Boolean{
        return url.contains("soundcloud.com")
    }
}
