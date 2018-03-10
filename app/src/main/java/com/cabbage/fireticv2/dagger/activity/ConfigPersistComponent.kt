package com.cabbage.fireticv2.dagger.activity

import com.cabbage.fireticv2.dagger.ConfigPersistent
import com.cabbage.fireticv2.dagger.app.AppComponent
import dagger.Component

@ConfigPersistent
@Component(dependencies = [AppComponent::class], modules = [ConfigPersistModule::class])
interface ConfigPersistComponent {

    fun activityComponent(activityModule: ActivityModule): ActivityComponent
}