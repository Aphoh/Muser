package com.aphoh.muser.util

/**
 * Created by Will on 7/1/2015.
 */
public class LogUtil(logTag: String) {
    private val logTag = logTag

    public fun d(message: String) {
        android.util.Log.d(logTag, message)
    }

}