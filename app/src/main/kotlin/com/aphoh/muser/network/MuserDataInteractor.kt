package com.aphoh.muser.network

import com.aphoh.muser.data.db.model.SongItem
import com.aphoh.muser.data.network.model.reddit.Oembed_
import com.aphoh.muser.util.LogUtil
import com.squareup.okhttp.OkHttpClient
import retrofit.RestAdapter
import retrofit.client.OkClient
import rx.Observable
import java.util.*


/**
 * Created by Will on 7/5/2015.
 */
public class MuserDataInteractor(var okClient: OkHttpClient, val soundcloudKeys: SoundcloudKeys) : DataInteractor {

    private val log = LogUtil(MuserDataInteractor::class.java.simpleName)

    private val redditService: RedditService = RestAdapter.Builder()
            .setEndpoint("http://www.reddit.com")
            .setClient(OkClient(okClient))
            .build().create(RedditService::class.java)

    private val soundcloudService: SoundcloudService = RestAdapter.Builder()
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
                    items.map { it.data }
                            .filter { isSoundcloudUrl(it.url) }
                            .filter { it.media != null }
                            .filter { it.media.oembed != null }
                            .filter { isTrack(it.media.oembed) }
                            .map { SongItem.fromPostData(it) }
                            .map { preProcess(it) }
                            .toArrayList()
                }
    }

    public override fun requestUrlForSongItem(songItem: SongItem): Observable<SongItem> {
        return soundcloudService.getSongFromUrl(songItem.linkUrl, soundcloudKeys.clientId)
                .map({ track ->
                    log.d("Track url returned: ${track.stream_url}")
                    var stream = "${track.stream_url}?client_id=${soundcloudKeys.clientId}"
                    songItem.length = track.duration
                    songItem.streamUrl = stream
                    songItem.waveformUrl = track.waveform_url
                    songItem
                })
    }

    private fun isSoundcloudUrl(url: String): Boolean {
        return url.startsWith("http://soundcloud.com") || url.startsWith("https://soundcloud.com") || url.startsWith("http://www.soundcloud.com") || url.startsWith("https://www.soundcloud.com")
    }

    private fun preProcess(songItem: SongItem): SongItem {
        //Replace &amp;
        if (songItem.artist != null)
            songItem.artist = songItem.artist.replaceAnd()
        if (songItem.songTitle != null)
            songItem.songTitle = songItem.songTitle.replaceAnd()
        return songItem
    }

    private fun String.replaceAnd(): String {
        if (this.contains("&amp;")) return replace("&amp;", "&")
        else return this
    }

    companion object Utils {
        public fun removeByLine(s: String): String = s.substringBeforeLast(" by")
        public fun isTrack(oembed: Oembed_): Boolean = kotlin.text.Regex(".*api\\.soundcloud\\.com(.....)tracks.*").containsMatchIn(oembed.html)
    }
}
