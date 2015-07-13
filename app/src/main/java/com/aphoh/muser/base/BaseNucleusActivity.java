package com.aphoh.muser.base;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.aphoh.muser.R;

import org.jetbrains.annotations.NotNull;

import nucleus.view.NucleusActivity;
import nucleus.view.NucleusAppCompatActivity;

/**
 * Created by Will on 7/1/2015.
 */
public abstract class BaseNucleusActivity<PresenterType extends BaseNucleusPresenter, DataType> extends NucleusAppCompatActivity<PresenterType> {
    Toolbar toolbar;

    protected Toolbar getToolbar() {
        return toolbar;
    }

    protected abstract int getLayoutId();

    protected int getToolbarId() {
        return R.id.toolbar;
    }

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(getLayoutId());
        toolbar = (Toolbar) findViewById(getToolbarId());
        setSupportActionBar(toolbar);
        getToolbar().setTitle("Muser");
    }

    public void toast(String msg){
        Toast.makeText(this, msg, msg.length() > 20 ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();

        int id = resources.getIdentifier(
                resources.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ?
                        "navigation_bar_height" :
                        "navigation_bar_height_landscape",
                "dimen",
                "android");
        if (id > 0) {
            return resources.getDimensionPixelSize(id);
        }
        return 0;
    }

    public abstract void publish(@NotNull DataType dataType);
}
