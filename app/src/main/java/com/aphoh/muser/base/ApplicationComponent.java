package com.aphoh.muser.base;

import com.aphoh.muser.music.MusicService;
import com.aphoh.muser.network.DataInteractor;
import com.aphoh.muser.network.SoundcloudKeys;
import com.aphoh.muser.ui.presenter.MainPresenter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Will on 7/12/15.
 */
@Singleton
@Component(modules = {DataModule.class})
public interface ApplicationComponent {
    void injectPresenter(MainPresenter presenter);
    void injectService(MusicService service);
    DataInteractor interactor();
    SoundcloudKeys keys();
}
