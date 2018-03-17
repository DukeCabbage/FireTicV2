package com.cabbage.fireticv2.presentation.main

import android.arch.lifecycle.ViewModel
import com.cabbage.fireticv2.data.FireTicRepository
import timber.log.Timber

class MainViewModel(private val repository: FireTicRepository) : ViewModel() {

    init {
        Timber.v("Init")
    }

    fun firebaseUser() = repository.getFirebaseUser()

    fun signOut() {
        Timber.w("Todo")
    }
}