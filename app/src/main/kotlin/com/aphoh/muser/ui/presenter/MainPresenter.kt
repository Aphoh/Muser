package com.aphoh.muser.ui.presenter

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.aphoh.muser.App
import com.aphoh.muser.base.BaseNucleusPresenter
import com.aphoh.muser.data.db.model.SongItem
import com.aphoh.muser.music.MusicService
import com.aphoh.muser.network.DataInteractor
import com.aphoh.muser.ui.activitiy.MainActivity
import com.aphoh.muser.util.LogUtil
import retrofit.RetrofitError
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by Will on 7/1/2015.
 */
public class MainPresenter : BaseNucleusPresenter<MainActivity, List<SongItem>>() {
    private var log = LogUtil(MainPresenter::class.java.simpleName)
    var dataInteractor: DataInteractor = App.applicationComponent.interactor()
    var transformer = App.applicationComponent.transformer()
    var subreddit: String = "trap"
    public var binder: MusicService.NotificationBinder? = null

    private var mMediaController: MediaControllerCompat? = null
    private var mSessionToken: MediaSessionCompat.Token? = null
    private var mTransportControls: MediaControllerCompat.TransportControls? = null

    private var serviceConnection: ServiceConnection? = null
    private var loading = AtomicBoolean(true)

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        loading.set(true)
        dataInteractor.refresh(subreddit)
                .compose(this.deliver<List<SongItem>>())
                .compose(transformer.get<List<SongItem>>())
                .subscribe(
                        { result ->
                            loading.set(false)
                            view.setRefreshing(loading.get())
                            view.publish(result)
                        },
                        { throwable ->
                            loading.set(false)
                            view.setRefreshing(loading.get())
                            log.e("Error retrieving songs", throwable)
                        })
    }

    override fun onTakeView(view: MainActivity) {
        super.onTakeView(view)
        if(!view.hasData()) refresh(view)
        autoBindOperation {
            //Get session token, subscribe to publish media events to UI
            mSessionToken = it.service.getSessionToken()
            mMediaController = MediaControllerCompat(view, mSessionToken)
            mMediaController?.let {
                it.registerCallback(callbacks)
                mTransportControls = it.transportControls
                if (it.metadata != null)
                    view.publishMetadata(it.metadata)
                if (it.playbackState != null)
                    view.publishPlaybackState(it.playbackState)
            }

        }
    }

    override fun dropView() {
        mMediaController?.apply {
            unregisterCallback(callbacks)
        }
        serviceConnection?.let {
            view.unbindService(it)
        }
        mMediaController = null
        mSessionToken = null
        mTransportControls = null
        binder = null
        super.dropView()
    }

    private val callbacks = object : MediaControllerCompat.Callback() {

        override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
            view.publishPlaybackState(state)
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat) {
            if (view != null) view.publishMetadata(metadata)
        }

        override fun onSessionDestroyed() {
            val callbacks = this
            autoBindOperation {
                if (mSessionToken == null || mSessionToken!!.equals(it.service.getSessionToken()))
                    mMediaController?.apply {
                        unregisterCallback(callbacks)
                    }
            }
        }
    }

    private fun autoBindOperation(action: (MusicService.NotificationBinder) -> Unit) {
        if (binder == null) {
            var intent = MusicService.getIntent(view)
            view.startService(intent)
            serviceConnection = object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder) {
                    log.d("Service bound")
                    binder = service as MusicService.NotificationBinder
                    action.invoke(binder!!)
                }

                override fun onServiceDisconnected(name: ComponentName?) {
                    binder = null
                }
            }
            view.bindService(intent, serviceConnection, Context.BIND_ABOVE_CLIENT)
        } else {
            binder?.apply {
                action.invoke(this)
            }
        }
    }

    /*
    * Exposed Methods
    * */

    public override fun refresh(view: MainActivity) {
        this.subreddit = view.getSubreddit()
        view.invalidateDataset()
        loading.set(true)
        view.setRefreshing(loading.get())
        dataInteractor.refresh(subreddit)
                .compose(this.deliver<List<SongItem>>())
                .compose(transformer.get<List<SongItem>>())
                .subscribe (
                        { result ->
                            loading.set(false)
                            view.setRefreshing(loading.get())
                            getView().publish(result)
                        },
                        { throwable ->
                            getView()?.let {
                                loading.set(false)
                                it.setRefreshing(loading.get())
                                var error = "Non-network error refreshing, this is a bug"
                                if (throwable is RetrofitError) {
                                    error = "Network Error ${throwable.response.status}, check your connection"
                                }
                                it.publishError(error)
                            }
                            log.e("Error refreshing", throwable)
                        })
    }

    public fun requestPlayAll(songItems: List<SongItem>) {
        autoBindOperation {
            log.d("Playing all songs")
            it.service.playSongs(songItems)
        }
    }

    public fun isPlaying(songItem: SongItem): Boolean {
        var playing = false
        autoBindOperation {
            playing = it.service.isPlaying(songItem)
        }
        return playing
    }

    public fun getCurrentDuration(): Long {
        var len = -1.toLong()
        autoBindOperation {
            if (it.service.mCurrentSong != null) {
                len = it.service.mCurrentSong!!.length
            }
        }
        return len
    }

    fun requestPause() = autoBindOperation {
        it.service.pause()
    }
    fun requestPlay() = autoBindOperation {
        it.service.play()
    }
    fun requestPrevious() = mTransportControls?.skipToPrevious()
    fun requestNext() = mTransportControls?.skipToNext()

}