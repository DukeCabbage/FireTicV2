package com.cabbage.fireticv2.dagger.activity

import com.cabbage.fireticv2.dagger.ActivityScope
import com.cabbage.fireticv2.presentation.base.MyViewModelFactory
import com.cabbage.fireticv2.presentation.home.HomeActivity
import com.cabbage.fireticv2.presentation.main.MainActivity
import com.cabbage.fireticv2.presentation.roomlist.RoomListActivity
import com.cabbage.fireticv2.presentation.stats.StatsActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [(ActivityModule::class)])
interface ActivityComponent {

    fun inject(mainActivity: MainActivity)

    fun inject(statsActivity: StatsActivity)

    fun inject(activity: RoomListActivity)

    fun inject(activity: HomeActivity)

    fun myViewModelFactory(): MyViewModelFactory
}