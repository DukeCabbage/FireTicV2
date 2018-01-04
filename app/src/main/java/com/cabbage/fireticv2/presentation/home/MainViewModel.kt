package com.cabbage.fireticv2.presentation.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber

class MainViewModel : ViewModel() {

    private val currentUser = MutableLiveData<FirebaseUser>()
    private val authStateListener = FirebaseAuth.AuthStateListener { currentUser.postValue(it.currentUser) }
    private var initialized = false

    private fun lazyInitialize() {
        if (!initialized) {
            Timber.d("Initializing...")
            FirebaseAuth.getInstance().addAuthStateListener(authStateListener)
            initialized = true
        }
    }

    fun getCurrentUser(): LiveData<FirebaseUser> {
        lazyInitialize()
        return currentUser
    }

    override fun onCleared() {
        super.onCleared()
        if (initialized) FirebaseAuth.getInstance().removeAuthStateListener(authStateListener)
    }
}