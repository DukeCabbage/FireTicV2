package com.cabbage.fireticv2.dagger.app

import android.content.Context
import com.cabbage.fireticv2.dagger.ApplicationScope
import com.cabbage.fireticv2.data.FireTicRepository
import com.cabbage.fireticv2.presentation.base.MyViewModelFactory
import dagger.Component
import javax.inject.Named

@ApplicationScope
@Component(modules = [
    AppModule::class,
    FirebaseModule::class
])
interface AppComponent {

    @Named("appContext")
    fun appContext(): Context

//    fun rxPreference(): RxSharedPreferences

    fun fireTicRepository(): FireTicRepository

    fun myViewModelFactory(): MyViewModelFactory
}