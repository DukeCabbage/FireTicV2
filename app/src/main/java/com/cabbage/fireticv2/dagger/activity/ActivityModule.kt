package com.cabbage.fireticv2.dagger.activity

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import com.cabbage.fireticv2.dagger.ActivityScope
import com.cabbage.fireticv2.dagger.ApplicationScope
import com.cabbage.fireticv2.data.FireTicRepository
import com.cabbage.fireticv2.presentation.home.MainVMFactory
import com.cabbage.fireticv2.presentation.home.MainViewModel
import com.cabbage.fireticv2.presentation.stats.UserAccountVMFactory
import com.cabbage.fireticv2.presentation.stats.UserAccountViewModel
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: AppCompatActivity) {

    @ActivityScope @Provides
    fun provideRxPermission(): RxPermissions {
        return RxPermissions(activity)
    }

    @ActivityScope @Provides
    fun provideMainViewModel(@ApplicationScope repo: FireTicRepository) =
            ViewModelProviders.of(activity, MainVMFactory(repo)).get(MainViewModel::class.java)

    @ActivityScope @Provides
    fun provideUserAccountViewModel(@ApplicationScope repo: FireTicRepository) =
            ViewModelProviders.of(activity, UserAccountVMFactory(repo)).get(UserAccountViewModel::class.java)
}