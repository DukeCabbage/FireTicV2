package com.cabbage.fireticv2

import android.app.Application
import com.cabbage.fireticv2.injection.AppComponent
import com.cabbage.fireticv2.injection.AppModule
import com.cabbage.fireticv2.injection.DaggerAppComponent
import com.facebook.stetho.Stetho
import timber.log.Timber

class MyApplication : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(ForestFire())

        Stetho.initializeWithDefaults(this)

        Timber.v("Initializing app component")
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }
}