package com.cabbage.fireticv2.dagger.activity

import com.cabbage.fireticv2.presentation.main.MainContract
import com.cabbage.fireticv2.presentation.main.MainPresenter
import dagger.Binds
import dagger.Module

@Module
abstract class ConfigPersistModule {

    @Binds
    abstract fun provideMainPresenter(presenter: MainPresenter): MainContract.Presenter
}