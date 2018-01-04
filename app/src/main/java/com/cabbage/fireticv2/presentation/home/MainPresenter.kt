package com.cabbage.fireticv2.presentation.home

import android.content.Context
import com.cabbage.fireticv2.presentation.base.BasePresenter
import timber.log.Timber

/**
 * Created by Leo on 2018-01-03.
 */
class MainPresenter : BasePresenter<MainContract.View>(),
                      MainContract.Presenter {

    init {
        Timber.v("init")
    }

    override fun requestVersionInfo(context: Context) {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val strVersion = "${packageInfo.versionName} (${packageInfo.versionCode})"
        mvpView?.displayVersionInfo(strVersion)
    }
}