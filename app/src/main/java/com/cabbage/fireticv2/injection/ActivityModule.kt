package com.cabbage.fireticv2.injection

import android.support.v7.app.AppCompatActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: AppCompatActivity) {

    @ActivityScope @Provides
    fun providesRxPermission(): RxPermissions {
        return RxPermissions(activity)
    }
}