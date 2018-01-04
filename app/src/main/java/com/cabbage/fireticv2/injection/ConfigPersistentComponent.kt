package com.cabbage.fireticv2.injection

import dagger.Component

@ConfigPersistent
@Component(dependencies = [(AppComponent::class)])
interface ConfigPersistentComponent {

    fun activityComponent(activityModule: ActivityModule): ActivityComponent
}