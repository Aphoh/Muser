package com.aphoh.muser.base;

import android.content.Context;
import com.aphoh.muser.BuildConfig;
import com.aphoh.muser.network.DataInteractor;
import com.aphoh.muser.network.MuserDataInteractor;
import com.aphoh.muser.network.SoundcloudKeys;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.OkHttpClient;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Created by Will on 7/12/15.
 */
@Module
public class DataModule {
  private final BaseApplication application;

  public DataModule(BaseApplication application) {
    this.application = application;
  }

  @Provides
  @Singleton
  Context provideApplicationContext() {
    return application;
  }

  @Provides
  @Singleton
  OkHttpClient provideHttpClient() {
    OkHttpClient client = new OkHttpClient();
    if (BuildConfig.DEBUG) client.networkInterceptors().add(new StethoInterceptor());
    return client;
  }

  @Provides
  @Singleton
  DataInteractor provideDataInteractor(Context context,
                                       OkHttpClient client,
                                       SoundcloudKeys keys) {
    return new MuserDataInteractor(context, client, keys);
  }

  @Provides
  @Singleton
  SoundcloudKeys provideSoundcloudKeys() {
    return new MuserSoundcloudKeys();
  }
}
