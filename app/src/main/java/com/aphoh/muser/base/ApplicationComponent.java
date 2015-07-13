package com.aphoh.muser.base;

import com.aphoh.muser.data.db.model.SongItem;
import com.aphoh.muser.music.MusicService;
import com.aphoh.muser.ui.activitiy.MainActivity;
import com.aphoh.muser.ui.presenter.MainPresenter;

import java.util.List;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Will on 7/12/15.
 */
@Singleton
@Component(modules = DataModule.class)
public interface ApplicationComponent {
    void injectPresenter(MainPresenter presenter);

    void injectService(MusicService service);
}
