package com.cabbage.fireticv2

import android.app.Application
import com.cabbage.fireticv2.dagger.app.AppComponent
import com.cabbage.fireticv2.dagger.app.AppModule
import com.cabbage.fireticv2.dagger.app.DaggerAppComponent
import com.cabbage.fireticv2.dagger.app.FirebaseModule
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
                .firebaseModule(FirebaseModule)
                .build()
    }
}