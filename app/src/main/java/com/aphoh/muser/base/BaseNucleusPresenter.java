package com.aphoh.muser.base;

import org.jetbrains.annotations.NotNull;

import nucleus.presenter.RxPresenter;

/**
 * Created by Will on 7/1/2015.
 */
public abstract class BaseNucleusPresenter<ViewType extends BaseNucleusActivity, DataType> extends RxPresenter<ViewType> {
    public abstract void refresh(@NotNull ViewType view);
}
