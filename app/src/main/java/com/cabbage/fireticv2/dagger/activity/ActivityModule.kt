package com.cabbage.fireticv2.dagger.activity

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import com.cabbage.fireticv2.dagger.ActivityScope
import com.cabbage.fireticv2.presentation.base.VMFactory
import com.cabbage.fireticv2.presentation.home.HomeViewModel
import com.cabbage.fireticv2.presentation.main.MainViewModel
import com.cabbage.fireticv2.presentation.roomlist.RoomListViewModel
import com.cabbage.fireticv2.presentation.stats.UserAccountViewModel
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: AppCompatActivity) {

//    @ActivityScope @Provides
//    fun provideRxPermission() = RxPermissions(activity)

    @ActivityScope @Provides
    fun provideHomeVM(factory: VMFactory): HomeViewModel {
        return ViewModelProviders.of(activity, factory).get(HomeViewModel::class.java)
    }

    @ActivityScope @Provides
    fun provideUserAccountVM(factory: VMFactory): UserAccountViewModel {
        return ViewModelProviders.of(activity, factory).get(UserAccountViewModel::class.java)
    }

    @ActivityScope @Provides
    fun provideRoomListVM(factory: VMFactory): RoomListViewModel {
        return ViewModelProviders.of(activity, factory).get(RoomListViewModel::class.java)
    }
}