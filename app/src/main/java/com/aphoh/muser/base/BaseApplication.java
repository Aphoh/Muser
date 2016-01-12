package com.aphoh.muser.base;

import android.app.Application;
import com.aphoh.muser.BuildConfig;

/**
 * Created by Will on 7/12/15.
 */
public abstract class BaseApplication extends Application {

  protected ApplicationComponent createApplicationComponent() {
    return DaggerApplicationComponent.builder()
        .dataModule(BuildConfig.mock ? new MockDataModule(this) : new DataModule(this))
        .rxModule(new RxModule())
        .build();
  }
}
