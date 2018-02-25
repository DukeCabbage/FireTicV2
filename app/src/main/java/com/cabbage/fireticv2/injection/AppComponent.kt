package com.cabbage.fireticv2.injection

import android.content.Context
import com.cabbage.fireticv2.data.FireTicRepository
import com.f2prateek.rx.preferences2.RxSharedPreferences
import dagger.Component
import javax.inject.Named

@ApplicationScope
@Component(modules = [(AppModule::class)])
interface AppComponent {

    @Named("appContext")
    fun appContext(): Context

    fun rxPreference(): RxSharedPreferences

    fun fireTicRepository(): FireTicRepository
}