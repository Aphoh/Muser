package com.aphoh.muser.base;

import android.app.Application;

/**
 * Created by Will on 7/12/15.
 */
public abstract class BaseApplication extends Application{

    protected ApplicationComponent createApplicationComponent(){
        return DaggerApplicationComponent.builder()
                .dataModule(new DataModule(this))
                .build();
    }
}
