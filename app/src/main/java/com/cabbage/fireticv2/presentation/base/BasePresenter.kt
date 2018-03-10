package com.cabbage.fireticv2.presentation.base

import android.support.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import timber.log.Timber

abstract class BasePresenter<V : MvpView>
    : MvpPresenter<V> {

    /**
     * Setting view to null will un-subscribe any on-going subscription
     */
    final override var mvpView: V? = null
        get() {
            if (field == null) Timber.w("No view attached yet")
            return field
        }

    @CallSuper
    override fun attachView(v: V) {
        Timber.v("attachView")
        mvpView = v
    }

    @CallSuper
    override fun detachView() {
        Timber.v("detachView")
        unSubscribeAll()
        mvpView = null
    }

    private var mDisposables: CompositeDisposable = CompositeDisposable()

    /**
     * Add a subscription, initialize the subscription container if needed
     */
    protected fun addSubscription(d: Disposable?) {
        if (d == null) return
        if (mDisposables.isDisposed) mDisposables = CompositeDisposable()
        mDisposables += d
    }

    /**
     * Un-subscribe a subscription, if it's not in the container, dispose anyway
     */
    protected fun unSubscribe(d: Disposable?) {
        if (d == null) return
        if (!(mDisposables.remove(d))) d.dispose()
    }

    /**
     * Un-subscribe everything in the container
     */
    protected fun unSubscribeAll() {
        mDisposables.dispose()
        mDisposables = CompositeDisposable()
    }
}