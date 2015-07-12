package com.aphoh.muser.ui.presenter

import android.os.Bundle
import com.aphoh.muser.base.BaseNucleusPresenter
import com.aphoh.muser.data.db.model.SongItem
import com.aphoh.muser.network.DataInteractor
import com.aphoh.muser.ui.activitiy.MainActivity
import com.facebook.stetho.okhttp.StethoInterceptor
import com.squareup.okhttp.OkHttpClient
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by Will on 7/1/2015.
 */
public class MainPresenter : BaseNucleusPresenter<MainActivity, List<SongItem>>() {
    var dataInteractor: DataInteractor? = null;

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        var ok = OkHttpClient()
        ok.networkInterceptors().add(StethoInterceptor())
        dataInteractor = DataInteractor(ok)
    }

    public override fun refresh(view: MainActivity) {
        var subreddit = "trap"
        dataInteractor!!.refresh(subreddit)
                .compose(this.deliver<List<SongItem>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    getView().publish(result)
                }
    }

    override fun onTakeView(view: MainActivity) {
        super.onTakeView(view)
        dataInteractor!!.getSongItems()
                .flatMap {
                    if (it.size() == 0) dataInteractor?.refresh("trap") else dataInteractor?.getSongItems()
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
}