package com.aphoh.muser.base;

import android.os.Bundle;

import nucleus.view.NucleusAppCompatActivity;

/**
 * Created by Will on 7/1/2015.
 */
public abstract class BaseNucleusActivity<PresenterType extends BaseNucleusPresenter> extends NucleusAppCompatActivity<PresenterType>{
    protected abstract int getLayoutId();

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(getLayoutId());
    }
}
