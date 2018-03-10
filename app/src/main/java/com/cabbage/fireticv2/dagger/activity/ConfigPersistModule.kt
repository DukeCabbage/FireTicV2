package com.cabbage.fireticv2.dagger.activity

import com.cabbage.fireticv2.presentation.home.MainContract
import com.cabbage.fireticv2.presentation.home.MainPresenter
import dagger.Binds
import dagger.Module

@Module
abstract class ConfigPersistModule {

    @Binds
    abstract fun provideMainPresenter(presenter: MainPresenter): MainContract.Presenter
}