package com.aphoh.muser.base;

import com.aphoh.muser.module.TransformerContainer;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Created by Will on 11/14/15.
 */
@Module public class RxModule {
  @Provides @Singleton
  TransformerContainer provideTransformerContainer() {
    return new TransformerContainer();
  }
}
