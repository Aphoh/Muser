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
import com.aphoh.muser.network.MuserDataInteractor
import com.aphoh.muser.ui.activitiy.MainActivity
import com.aphoh.muser.util.LogUtil
import retrofit.RetrofitError
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Will on 7/1/2015.
 */
public class MainPresenter : BaseNucleusPresenter<MainActivity, List<SongItem>>() {
    private var log = LogUtil(javaClass<MainPresenter>().simpleName)
    var dataInteractor: MuserDataInteractor? = null
        @Inject set
    var mMusicServiceBinder: MusicService.MusicBinder? = null
    var subreddit: String = "trap"

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
        mMusicServiceBinder?.getInteractor()?.stop()
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

    override fun onDestroy() {
        val musicInteractor = mMusicServiceBinder?.musicInteractor

        var playing = musicInteractor?.mIsPlaying
        if (playing != null && !playing) {
            musicInteractor?.destroy()
        }
    }

    public fun onSongSelected(songItem: SongItem) {
        if (songItem.getStreamUrl() == null) {
            dataInteractor!!.requestUrlForSongItem(songItem)
                    .compose(this.deliver<SongItem>())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ item ->
                        log.d("item stream url: ${item.streamUrl}")
                        if (getView() != null) {
                            getView().publishSongPlay(songItem)
                            notifyStartService()
                        }
                    }, { error ->
                        if (error is RetrofitError) {
                            var code = error.response.status
                            if (code == 404) getView().toast("Track not found")
                        }
                        log.e("Failed to retrieve stream url: ${songItem.linkUrl}", error)
                    })
        } else {
            getView().publishSongPlay(songItem)
        }
    }

    private fun notifyStartService(songItems: List<SongItem>) {

        if (!isMyServiceRunning(javaClass<MusicService>().name)) {
            var intent = Intent(getView(), javaClass<MusicService>())
            getView().startService(intent)
            getView().bindService(intent, object:ServiceConnection{
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    if(service is MusicService.MusicBinder){
                        mMusicServiceBinder = service
                        service.musicInteractor.mSongs = songItems
                        service.musicInteractor.playFromStart()
                    }
                }

                override fun onServiceDisconnected(name: ComponentName?) {
                    mMusicServiceBinder = null
                }
            }, 0)
        } else {
            getView().bindService(Intent(getView(), javaClass<MusicService>()), object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    if(service is MusicService.MusicBinder) {
                        mMusicServiceBinder = service
                        service.musicInteractor.mSongs = songItems
                        service.musicInteractor.playFromStart()
                    }
                }

                override fun onServiceDisconnected(name: ComponentName?) {
                    mMusicServiceBinder = null
                }
            }, 0)
        }
    }

    private fun notifyServiceStarted() {
        var interactor = mMusicServiceBinder!!.musicInteractor
        if (interactor.mIsPlaying) {
            getView().publishSongPlay(interactor.getCurrentSong()!!)
            interactor.mSongChangedListener = { prev, next ->
                if (getView() != null)
                    getView().publishSongPlay(next)
            }
        } else {
            interactor.mSongs =
        }
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
}