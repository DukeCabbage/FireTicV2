package com.cabbage.fireticv2.dagger.app

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.cabbage.fireticv2.dagger.ApplicationScope
import com.cabbage.fireticv2.data.FireTicRepository
import com.cabbage.fireticv2.presentation.base.MyViewModelFactory
import com.f2prateek.rx.preferences2.RxSharedPreferences
import dagger.Module
import dagger.Provides
import timber.log.Timber
import javax.inject.Named

@Module
class AppModule(private val appContext: Context) {

    init {
        if (appContext is Activity || appContext is Service) {
            Timber.w("Expects application context")
        }
    }

    @ApplicationScope @Provides
    @Named("appContext")
    fun provideContext(): Context {
        return appContext
    }

    @Module
    companion object {
        @ApplicationScope @Provides @JvmStatic
        fun providePreference(@Named("appContext") context: Context): SharedPreferences {
            return context.getSharedPreferences("default", MODE_PRIVATE)
        }

        @ApplicationScope @Provides @JvmStatic
        fun provideRxPermission(@Named("appContext") context: Context): RxSharedPreferences {
            val sharedPreferences = context.getSharedPreferences("default", MODE_PRIVATE)
            return RxSharedPreferences.create(sharedPreferences)
        }
    }
}