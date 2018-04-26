package com.cabbage.fireticv2.presentation.stats

import android.arch.lifecycle.ViewModel
import com.cabbage.fireticv2.data.FireTicRepository
import timber.log.Timber

class UserAccountViewModel(private val repository: FireTicRepository) : ViewModel() {

    init {
        Timber.v("Init")
    }

    fun firebaseUser() = repository.getFirebaseUser()

    fun signedInUser() = repository.getCurrentUser()

    fun updateUserName(newName: String) {

        val user = signedInUser().value
        if (user != null) {
            repository.userRepo.updateUserName(user.userId, newName)
        } else {
            Timber.w("No signed in user")
        }
    }
}