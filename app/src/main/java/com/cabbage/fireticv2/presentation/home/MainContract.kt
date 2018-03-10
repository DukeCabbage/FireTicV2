package com.cabbage.fireticv2.presentation.home

import android.content.Context
import com.cabbage.fireticv2.presentation.base.MvpPresenter
import com.cabbage.fireticv2.presentation.base.MvpView
import com.google.firebase.auth.FirebaseUser

interface MainContract {

    interface View : MvpView {
        fun displayVersionInfo(info: String)

        fun toastMessage(msg: String)
    }

    interface Presenter : MvpPresenter<View> {
        fun requestVersionInfo(context: Context)

        fun signInAnonymously()

        fun checkIfUserExistsInFirestore(user: FirebaseUser)
    }
}