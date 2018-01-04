package com.cabbage.fireticv2.presentation.home

import android.content.Context
import com.cabbage.fireticv2.presentation.base.BasePresenter
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

class MainPresenter constructor() : BasePresenter<MainContract.View>(),
                                    MainContract.Presenter {

    init {
        Timber.v("init")
    }

    constructor(view: MainContract.View) : this() {
        this.attachView(view)
    }

    override fun requestVersionInfo(context: Context) {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val strVersion = "${packageInfo.versionName} (${packageInfo.versionCode})"
        mvpView?.displayVersionInfo(strVersion)
    }

    override fun signInAnonymously() {
        FirebaseAuth.getInstance().signInAnonymously()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Timber.i("signInAnonymously success")
                        mvpView?.toastMessage("signInAnonymously success")
                    } else {
                        Timber.e(task.exception)
                        mvpView?.toastMessage(task.exception?.localizedMessage ?: "signInAnonymously fail")
                    }
                }
    }
}