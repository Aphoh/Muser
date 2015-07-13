package com.aphoh.muser

import android.app.Application
import com.aphoh.muser.base.ApplicationComponent
import com.aphoh.muser.base.BaseApplication
import com.aphoh.muser.base.DataModule
import com.facebook.stetho.Stetho
import com.raizlabs.android.dbflow.config.FlowManager
import kotlin.platform.platformStatic
import kotlin.properties.Delegates

/**
 * Created by Will on 7/11/2015.
 */
public class App : BaseApplication() {

    companion object{
        platformStatic public var applicationComponent : ApplicationComponent by Delegates.notNull()
    }

    override fun onCreate() {
        super<BaseApplication>.onCreate()
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this)).build())
        FlowManager.init(this)
        applicationComponent = createApplicationComponent()
    }
}