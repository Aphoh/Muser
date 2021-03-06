package com.aphoh.muser.network.interactors

import android.content.Context
import android.net.ConnectivityManager
import com.aphoh.muser.data.db.model.SongItem
import com.aphoh.muser.data.network.LoganSquareConverter
import com.aphoh.muser.data.network.model.reddit.Oembed_
import com.aphoh.muser.network.NetworkException
import com.aphoh.muser.network.SortingConfig
import com.aphoh.muser.network.SoundcloudKeys
import com.aphoh.muser.network.retrofit.RedditService
import com.aphoh.muser.network.retrofit.SoundcloudService
import com.aphoh.muser.util.LogUtil
import com.squareup.okhttp.OkHttpClient
import retrofit.RestAdapter
import retrofit.client.OkClient
import rx.Observable
import java.util.*


/**
 * Created by Will on 7/5/2015.
 */
class MuserDataInteractor(val context: Context, val okClient: OkHttpClient, val soundcloudKeys: SoundcloudKeys) : DataInteractor {

    private val log = LogUtil(MuserDataInteractor::class.java.simpleName)

    private val redditService: RedditService = RestAdapter.Builder()
            .setConverter(LoganSquareConverter())
            .setEndpoint("http://www.reddit.com")
            .setClient(OkClient(okClient))
            .build().create(RedditService::class.java)

    private val soundcloudService: SoundcloudService = RestAdapter.Builder()
            .setConverter(LoganSquareConverter())
            .setEndpoint("http://api.soundcloud.com")
            .setClient(OkClient(okClient))
            .build().create(SoundcloudService::class.java)

    // ===========================================
    // Network Calls
    // ===========================================


    override fun refresh(subreddit: String, sortingConfig: SortingConfig): Observable<ArrayList<SongItem>> =
            if (hasNetwork())
                redditService.getSubredditSubmissions(
                        subreddit = subreddit,
                        category = sortingConfig.category.string,
                        time = sortingConfig.time?.string,
                        limit = 100)
                        .map { it.data.postItems }
                        .map { items ->
                            items.map { it.data }
                                    .filter { isSoundcloudUrl(it.url) }
                                    .filter { it.media != null }
                                    .filter { it.media.oembed != null }
                                    .filter { isTrack(it.media.oembed) }
                                    .map { SongItem.fromPostData(it) }
                                    .map { preProcess(it) }
                                    .toCollection(ArrayList())
                        }
            else
                Observable.error(NetworkException.from("No Available Networks"))

    override fun requestUrlForSongItem(songItem: SongItem): Observable<SongItem> {
        if (hasNetwork())
            return soundcloudService.getSongFromUrl(songItem.linkUrl, soundcloudKeys.clientId)
                    .map({ track ->
                        log.d("Track url returned: ${track.stream_url}")
                        var stream = "${track.stream_url}?client_id=${soundcloudKeys.clientId}"
                        songItem.length = track.duration
                        songItem.streamUrl = stream
                        songItem.waveformUrl = track.waveform_url
                        songItem
                    })
        else
            return Observable.error(NetworkException.from("No Available Networks"))
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

    private fun hasNetwork(): Boolean {
        val info = (context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager).activeNetworkInfo
        return info != null && info.isConnectedOrConnecting
    }

    companion object Utils {
        fun removeByLine(s: String): String = s.substringBeforeLast(" by")
        fun isTrack(oembed: Oembed_): Boolean = Regex(".*api\\.soundcloud\\.com(.....)tracks.*").containsMatchIn(oembed.html)
    }

}
