package com.aphoh.muser.ui.presenter

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import com.aphoh.muser.App
import com.aphoh.muser.base.BaseNucleusPresenter
import com.aphoh.muser.data.db.model.SongItem
import com.aphoh.muser.music.MusicService
import com.aphoh.muser.music.MusicView
import com.aphoh.muser.network.MuserDataInteractor
import com.aphoh.muser.ui.activitiy.MainActivity
import com.aphoh.muser.util.LogUtil
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

/**
 * Created by Will on 7/1/2015.
 */
public class MainPresenter : BaseNucleusPresenter<MainActivity, List<SongItem>>() {
    private var log = LogUtil(javaClass<MainPresenter>().simpleName)
    var dataInteractor: MuserDataInteractor? = null
        @Inject set
    var subreddit: String = "trap"
    public var binder: MusicService.NotificationBinder? = null

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        App.applicationComponent.injectPresenter(this)
        dataInteractor!!.getSongItems()
                .flatMap {
                    if (it.size() == 0) dataInteractor?.refresh(subreddit) else dataInteractor?.getSongItems()
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
        subreddit = "trap"
        dataInteractor!!.refresh(subreddit)
                .compose(this.deliver<List<SongItem>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                        { result ->
                            getView().publish(result)
                        },
                        { throwable ->
                            log.e("Error refreshing", throwable)
                        })
    }

    public fun onSongSelected(songItem: SongItem) {
        autoBindOperation {
            it?.service?.playSongs(Arrays.asList(songItem))
        }
    }

    private fun autoBindOperation(action: (MusicService.NotificationBinder?) -> Unit) {
        if (binder == null) {
            var intent = MusicService.getIntent(view)
            getView().bindService(intent, object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    binder = service as MusicService.NotificationBinder
                    action.invoke(binder)
                }

                override fun onServiceDisconnected(name: ComponentName?) {
                    binder = null
                }
            },
                    Context.BIND_ABOVE_CLIENT)
        } else {
            action.invoke(binder)
        }
    }
}