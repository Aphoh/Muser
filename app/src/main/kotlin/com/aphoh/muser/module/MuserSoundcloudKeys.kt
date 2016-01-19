package com.aphoh.muser.module

import com.aphoh.muser.BuildConfig
import com.aphoh.muser.network.SoundcloudKeys

/**
 * Created by Will on 9/30/15.
 */
class MuserSoundcloudKeys : SoundcloudKeys {
    override val clientId: String = BuildConfig.SC_CLIENT_ID
}
