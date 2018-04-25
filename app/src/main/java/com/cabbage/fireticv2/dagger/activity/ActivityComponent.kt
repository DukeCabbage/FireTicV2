package com.cabbage.fireticv2.dagger.activity

import com.cabbage.fireticv2.dagger.ActivityScope
import com.cabbage.fireticv2.data.FireTicRepository
import com.cabbage.fireticv2.presentation.base.VMFactory
import com.cabbage.fireticv2.presentation.home.HomeActivity
import com.cabbage.fireticv2.presentation.roomlist.RoomListActivity
import com.cabbage.fireticv2.presentation.stats.StatsActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [(ActivityModule::class)])
interface ActivityComponent {

    fun inject(statsActivity: StatsActivity)

    fun inject(activity: RoomListActivity)

    fun inject(activity: HomeActivity)

    fun repository(): FireTicRepository

    fun myViewModelFactory(): VMFactory
}