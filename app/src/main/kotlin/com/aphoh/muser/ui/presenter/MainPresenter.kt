package com.aphoh.muser.ui.presenter

import android.os.Bundle
import com.aphoh.muser.base.BaseNucleusPresenter
import com.aphoh.muser.data.db.model.SongItem
import com.aphoh.muser.network.DataInteractor
import com.aphoh.muser.ui.activitiy.MainActivity
import com.aphoh.muser.util.LogUtil
import com.facebook.stetho.okhttp.StethoInterceptor
import com.squareup.okhttp.OkHttpClient
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by Will on 7/1/2015.
 */
public class MainPresenter : BaseNucleusPresenter<MainActivity, List<SongItem>>() {
    private var log = LogUtil(javaClass<MainPresenter>().getSimpleName())
    private var dataInteractor: DataInteractor? = null;
    var subreddit: String = "trap"

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        var ok = OkHttpClient()
        ok.networkInterceptors().add(StethoInterceptor())
        dataInteractor = DataInteractor(ok)
        dataInteractor!!.getSongItems()
                .flatMap {
                    if (it.size() == 0) dataInteractor?.refresh(subreddit) else dataInteractor?.getSongItems()
                }
                .compose(this.deliver<List<SongItem>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (getView() != null) {
                        getView().publish(result)
                    }
                })
    }

    public override fun refresh(view: MainActivity) {
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
                        if(getView() != null) {
                            getView().toast("ayy lmao")
                            getView().publishSongPlay(songItem)
                        }
                    }, { error ->
                        log.e("Failed to retrieve stream url: ${songItem.getUrl()}", error)
                    })
        }else{
            getView().publishSongPlay(songItem)
        }
    }

    override fun onTakeView(view: MainActivity) {
        super.onTakeView(view)

    }
}