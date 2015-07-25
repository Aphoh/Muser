package com.aphoh.muser.base;

import android.content.Context;

import com.aphoh.muser.App;
import com.aphoh.muser.BuildConfig;
import com.aphoh.muser.network.DataInteractor;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Will on 7/12/15.
 */
@Module
public class DataModule {
    private final BaseApplication application;

    public DataModule(BaseApplication application){
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext(){
        return application;
    }

    @Provides
    @Singleton
    OkHttpClient provideHttpClient(){
        OkHttpClient client = new OkHttpClient();
        if(BuildConfig.DEBUG) client.networkInterceptors().add(new StethoInterceptor());
        return client;
    }

    @Provides
    @Singleton
    DataInteractor provideDataInteractor(OkHttpClient client){
        return new DataInteractor(client);
    }

}