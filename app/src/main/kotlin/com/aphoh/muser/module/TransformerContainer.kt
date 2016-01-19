package com.aphoh.muser.module

import rx.Observable
import rx.Observable.Transformer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by Will on 11/14/15.
 */
class TransformerContainer {
    fun <T> get(): Transformer<T, T> {
        return Transformer { tObservable -> tObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }
    }
}
