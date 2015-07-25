package com.aphoh.muser.base;

import com.aphoh.muser.music.MusicInteractor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Will on 7/25/15.
 */
@Module
public class MusicModule {

    private final BaseApplication appContext;

    public MusicModule(BaseApplication appContext) {
        this.appContext = appContext;
    }

    @Provides
    @Singleton
    public MusicInteractor provideMusicInteractor(){
        return new MusicInteractor();
    }
}
