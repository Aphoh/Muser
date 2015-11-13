package com.aphoh.muser.util

/**
 * Created by Will on 7/1/2015.
 */
public class LogUtil(logTag: String) {
    private val logTag = logTag

    public infix fun d(message: String) {
        android.util.Log.d(logTag, message)
    }

    public fun e(message: String){
        android.util.Log.e(logTag, message)
    }

    public fun e(throwable: Throwable){
        android.util.Log.e(logTag,
                android.util.Log.getStackTraceString(throwable))
    }

    public fun e(message: String, throwable: Throwable){
        android.util.Log.e(logTag,
                "${message}\nStacktrace: \n${android.util.Log.getStackTraceString(throwable)}")
    }

}