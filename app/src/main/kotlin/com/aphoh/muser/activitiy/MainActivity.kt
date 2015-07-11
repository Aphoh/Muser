package com.aphoh.muser.activitiy

import android.os.Bundle
import com.aphoh.muser.R
import com.aphoh.muser.base.BaseNucleusActivity
import com.aphoh.muser.presenter.MainPresenter


public class MainActivity : BaseNucleusActivity<MainPresenter>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
