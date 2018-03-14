package com.cabbage.fireticv2

import android.app.Application
import android.content.Context
import com.cabbage.fireticv2.dagger.app.AppComponent
import com.cabbage.fireticv2.dagger.app.AppModule
import com.cabbage.fireticv2.dagger.app.DaggerAppComponent
import com.cabbage.fireticv2.dagger.app.FirebaseModule
import com.facebook.stetho.Stetho
import timber.log.Timber

class MyApplication : Application() {

    companion object {
        fun getInstance(context: Context) = context.applicationContext as MyApplication
    }

    val appComponent: AppComponent by lazy {
        Timber.v("Initializing app component")

        DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .firebaseModule(FirebaseModule)
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(ForestFire())

        Stetho.initializeWithDefaults(this)
    }
}