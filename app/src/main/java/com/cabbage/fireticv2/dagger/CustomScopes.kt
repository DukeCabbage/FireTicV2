package com.cabbage.fireticv2.dagger

import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ConfigPersistent

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope