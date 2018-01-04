package com.cabbage.fireticv2.presentation.home

import android.content.Context
import com.cabbage.fireticv2.presentation.base.MvpPresenter
import com.cabbage.fireticv2.presentation.base.MvpView

/**
 * Created by Leo on 2018-01-03.
 */
interface MainContract {

    interface View : MvpView {
        fun displayVersionInfo(info: String)
    }

    interface Presenter : MvpPresenter<View> {
        fun requestVersionInfo(context: Context)
    }
}