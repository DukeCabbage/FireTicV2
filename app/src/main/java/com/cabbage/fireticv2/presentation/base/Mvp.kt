package com.cabbage.fireticv2.presentation.base

interface MvpView

interface MvpPresenter<V : MvpView> {

    var mvpView: V?

    fun attachView(v: V)

    fun detachView()
}
