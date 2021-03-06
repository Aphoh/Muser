package com.aphoh.muser

import com.aphoh.muser.base.ApplicationComponent
import com.aphoh.muser.base.BaseApplication
import com.facebook.stetho.Stetho
import kotlin.properties.Delegates

/**
 * Created by Will on 7/11/2015.
 */
public class App : BaseApplication() {

    companion object {
        @JvmStatic public var applicationComponent: ApplicationComponent by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this)).build())
        applicationComponent = createApplicationComponent()
    }
}