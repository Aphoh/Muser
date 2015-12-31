package com.aphoh.muser.base;

import nucleus.presenter.RxPresenter;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Will on 7/1/2015.
 */
public abstract class BaseNucleusPresenter<ViewType extends BaseNucleusActivity, DataType>
    extends RxPresenter<ViewType> {
  public abstract void refresh(@NotNull ViewType view);
}
