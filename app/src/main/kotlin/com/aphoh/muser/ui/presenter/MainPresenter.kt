package com.aphoh.muser.ui.presenter

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
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
import javax.inject.Inject

/**
 * Created by Will on 7/1/2015.
 */
public class MainPresenter : BaseNucleusPresenter<MainActivity, List<SongItem>>(), ServiceConnection {
    private var log = LogUtil(javaClass<MainPresenter>().getSimpleName())
    var dataInteractor: DataInteractor? = null
        @Inject set
    var mMusicServiceBinder: MusicService.MusicBinder? = null
    var subreddit: String = "trap"

    override fun onCreate(savedState: Bundle?) {
        super<BaseNucleusPresenter>.onCreate(savedState)
        App.applicationComponent.injectPresenter(this)
        dataInteractor!!.getSongItems()
                .flatMap {
                    if (it.size() == 0) dataInteractor?.refresh(subreddit) else dataInteractor?.getSongItems()
                }
                .compose(this.deliverLatestCache<List<SongItem>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (getView() != null) {
                        getView().publish(result)
                    }
                })
    }

    public override fun refresh(view: MainActivity) {
        mMusicServiceBinder?.getMusicInteractor()?.destroy()
        subreddit = "trap"
        dataInteractor!!.refresh(subreddit)
                .compose(this.deliver<List<SongItem>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    getView().publish(result)
                }
    }

    public fun onSongSelected(songItem: SongItem) {
        if (songItem.getStreamUrl() == null) {
            dataInteractor!!.requestUrlForSongItem(songItem)
                    .compose(this.deliver<SongItem>())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ item ->
                        log.d("item stream url: ${item.getStreamUrl()}")
                        if (getView() != null) {
                            getView().publishSongPlay(songItem)
                            notifyStartService()
                        }
                    }, { error ->
                        if (error is RetrofitError) {
                            var code = error.getResponse().getStatus()
                            if (code == 404) getView().toast("Track not found")
                        }
                        log.e("Failed to retrieve stream url: ${songItem.getUrl()}", error)
                    })
        } else {
            getView().publishSongPlay(songItem)
            notifyStartService()
        }
    }

    private fun notifyStartService() {
        if (!isMyServiceRunning(javaClass<MusicService>().getName())) {
            var intent = Intent(getView(), javaClass<MusicService>())
            getView().startService(intent)
            getView().bindService(intent, this, 0)
        } else {
            getView().bindService(Intent(getView(), javaClass<MusicService>()), this, 0)
        }
    }

    private fun notifyServiceStarted() {
        var interactor = mMusicServiceBinder!!.getMusicInteractor()
        if(interactor.mIsPlaying){
            getView().publishSongPlay(interactor.getCurrentSong()!!)
            interactor.mSongChangedListener = { prev, next ->
                getView().publishSongPlay(next)
            }
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        mMusicServiceBinder = service as MusicService.MusicBinder
        notifyServiceStarted()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        mMusicServiceBinder = null
    }

    private fun isMyServiceRunning(className: String): Boolean {
        var manager = getView().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (serviceInfo in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceInfo.service.getClassName().equals(className)) {
                return true
            }
        }
        return false
    }

    override fun onTakeView(view: MainActivity) {
        super<BaseNucleusPresenter>.onTakeView(view)
    }
}