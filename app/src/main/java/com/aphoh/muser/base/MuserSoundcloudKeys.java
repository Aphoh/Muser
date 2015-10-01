package com.aphoh.muser.base;

import com.aphoh.muser.BuildConfig;
import com.aphoh.muser.network.SoundcloudKeys;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Will on 9/30/15.
 */
public class MuserSoundcloudKeys implements SoundcloudKeys{
    @NotNull
    @Override
    public String getClientId() {
        return BuildConfig.SC_CLIENT_ID;
    }
}
