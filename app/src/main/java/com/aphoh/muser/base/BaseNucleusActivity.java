package com.aphoh.muser.base;

import android.os.Bundle;

import org.jetbrains.annotations.NotNull;

import nucleus.view.NucleusActivity;

/**
 * Created by Will on 7/1/2015.
 */
public abstract class BaseNucleusActivity<PresenterType extends BaseNucleusPresenter, DataType> extends NucleusActivity<PresenterType> {
    protected abstract int getLayoutId();

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(getLayoutId());
    }

    public abstract void publish(@NotNull DataType dataType);
}
