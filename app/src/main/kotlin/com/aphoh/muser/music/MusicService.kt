package com.aphoh.muser.music

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Created by Will on 7/12/15.
 */
public class MusicService() : Service(){
    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException()
    }
}