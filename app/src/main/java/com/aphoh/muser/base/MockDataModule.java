package com.aphoh.muser.base;

import android.content.Context;
import com.aphoh.muser.network.DataInteractor;
import com.aphoh.muser.network.MockDataInteractor;
import com.aphoh.muser.network.SoundcloudKeys;
import com.squareup.okhttp.OkHttpClient;

/**
 * Created by Will on 1/5/16.
 */
public class MockDataModule extends DataModule {

  public MockDataModule(BaseApplication application) {
    super(application);
  }

  Context provideApplicationContext() {
    return super.provideApplicationContext();
  }

  OkHttpClient provideHttpClient() {
    return super.provideHttpClient();
  }

  DataInteractor provideDataInteractor(OkHttpClient client, SoundcloudKeys keys) {
    return new MockDataInteractor();
  }

  SoundcloudKeys provideSoundcloudKeys() {
    return super.provideSoundcloudKeys();
  }
}
