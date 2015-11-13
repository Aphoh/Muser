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
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by Will on 7/1/2015.
 */
public class MainPresenter : BaseNucleusPresenter<MainActivity, List<SongItem>>() {
    private var log = LogUtil(MainPresenter::class.java.simpleName)
    var dataInteractor: DataInteractor = App.applicationComponent.interactor()
    var subreddit: String = "trap"
    public var binder: MusicService.NotificationBinder? = null
    private var serviceConnection: ServiceConnection? = null

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        dataInteractor.getSongItems()
                .flatMap {
                    if (it.size == 0) dataInteractor.refresh(subreddit) else dataInteractor.getSongItems()
                }
                .compose(this.deliverLatestCache<List<SongItem>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            if (getView() != null) {
                                getView().publish(result)
                            }
                        },
                        { throwable ->
                            log.e("Error retrieving songs", throwable)
                        })
    }

    public override fun refresh(view: MainActivity) {
        this.subreddit = view.getSubreddit()
        dataInteractor.refresh(subreddit)
                .compose(this.deliver<List<SongItem>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                        { result ->
                            if (getView() != null) {
                                getView().publish(result)
                            }
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
        autoBindOperation { it.service.bind(view) }
    }

    override fun dropView() {
        /*Do here so it's called before getView() is null*/
        if (binder != null) {
            var bound = binder?.service?.isBound(getView())
            if (bound != null && bound) binder?.service?.unbind(getView())
        }
        if (serviceConnection != null) getView().unbindService(serviceConnection)
        binder = null
        super.dropView()
    }

    public fun requestPlayAll(songItems: List<SongItem>) {
        autoBindOperation {
            log.d("Playing all songs")
            it.service.playSongs(songItems)
        }
    }

    private fun autoBindOperation(action: (MusicService.NotificationBinder) -> Unit) {
        if (binder == null) {
            var intent = MusicService.getIntent(view)
            getView().startService(intent)
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
            getView().bindService(intent, serviceConnection, Context.BIND_ABOVE_CLIENT)
        } else {
            action.invoke(binder!!)
        }
    }
}