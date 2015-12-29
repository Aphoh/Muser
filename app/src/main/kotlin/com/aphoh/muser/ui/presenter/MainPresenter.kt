package com.aphoh.muser.ui.presenter

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import com.aphoh.muser.App
import com.aphoh.muser.base.BaseNucleusPresenter
import com.aphoh.muser.data.db.model.SongItem
import com.aphoh.muser.music.MusicService
import com.aphoh.muser.network.DataInteractor
import com.aphoh.muser.ui.activitiy.MainActivity
import com.aphoh.muser.util.LogUtil
import retrofit.RetrofitError

/**
 * Created by Will on 7/1/2015.
 */
public class MainPresenter : BaseNucleusPresenter<MainActivity, List<SongItem>>() {
    private var log = LogUtil(MainPresenter::class.java.simpleName)
    var dataInteractor: DataInteractor = App.applicationComponent.interactor()
    var transformer = App.applicationComponent.transformer()
    var subreddit: String = "trap"
    public var binder: MusicService.NotificationBinder? = null
    private var serviceConnection: ServiceConnection? = null

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        dataInteractor.refresh(subreddit)
                .compose(this.deliver<List<SongItem>>())
                .compose(transformer.get<List<SongItem>>())
                .subscribe(
                        { result ->
                            view.publish(result)
                        },
                        { throwable ->
                            log.e("Error retrieving songs", throwable)
                        })
    }

    public override fun refresh(view: MainActivity) {
        this.subreddit = view.getSubreddit()
        view.invalidateDataset()
        dataInteractor.refresh(subreddit)
                .compose(this.deliver<List<SongItem>>())
                .compose(transformer.get<List<SongItem>>())
                .subscribe (
                        { result ->
                            getView().publish(result)
                        },
                        { throwable ->
                            getView()?.let {
                                var error = "Non-network error refreshing, this is a bug"
                                if (throwable is RetrofitError) {
                                    error = "Network Error ${throwable.response.status}, check your connection"
                                }
                                it.publishError(error)
                            }
                            log.e("Error refreshing", throwable)
                        })
    }

    override fun onTakeView(view: MainActivity) {
        super.onTakeView(view)
        autoBindOperation {
            it.service.bind(view)
            it.service.bind(view.playPauseView)
        }
    }

    override fun dropView() {
        /*Do here so it's called before getView() is null*/
        binder?.apply {
            if (service.isBound(view))
                service.unbind(view)
            if (service.isBound(view.playPauseView))
                service.unbind(view.playPauseView)
        }
        serviceConnection?.let {
            view.unbindService(it)
        }
        binder = null
        super.dropView()
    }

    public fun requestPlayAll(songItems: List<SongItem>) {
        autoBindOperation {
            log.d("Playing all songs")
            it.service.playSongs(songItems)
        }
    }

    public fun isPlaying(songItem: SongItem): Boolean{
        var playing = false
        autoBindOperation {
            playing = it.service.isPlaying(songItem)
        }
        return playing
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
}