package com.cabbage.fireticv2.presentation.home

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.content.Context
import com.cabbage.fireticv2.data.FireTicRepository
import com.cabbage.fireticv2.dagger.ConfigPersistent
import com.cabbage.fireticv2.presentation.base.BasePresenter
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber
import javax.inject.Inject

@ConfigPersistent
class MainPresenter
@Inject constructor(private val repository: FireTicRepository)
    : BasePresenter<MainContract.View>(),
      MainContract.Presenter {

    init {
        Timber.v("Init")
    }

    override fun requestVersionInfo(context: Context) {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val strVersion = "${packageInfo.versionName} (${packageInfo.versionCode})"
        mvpView?.displayVersionInfo(strVersion)
    }

    override fun signInAnonymously() {
        repository.auth.signInAnonymously()
                .addOnSuccessListener {
                    Timber.i("signInAnonymously success")
                    mvpView?.toastMessage("signInAnonymously success")
                }
                .addOnFailureListener {
                    Timber.e(it)
                    mvpView?.toastMessage(it.localizedMessage ?: "signInAnonymously fail")
                }
    }

    override fun checkIfUserExistsInFirestore(user: FirebaseUser) {
        repository.userRepository.getUser(user.uid)
                .observe(this.mvpView as LifecycleOwner, Observer {
                    if (it == null) {
                        repository.userRepository.createUser(user.uid)
                    } else {
                        Timber.w(it.name)
                        mvpView?.toastMessage("signed in as ${it.name}")
                    }
                })
    }
}