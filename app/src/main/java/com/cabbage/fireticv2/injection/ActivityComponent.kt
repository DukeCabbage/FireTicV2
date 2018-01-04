package com.cabbage.fireticv2.injection

import com.cabbage.fireticv2.presentation.home.MainActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [(ActivityModule::class)])
interface ActivityComponent {

    fun inject(mainActivity: MainActivity)
}