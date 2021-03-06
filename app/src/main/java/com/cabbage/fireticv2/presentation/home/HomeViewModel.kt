package com.cabbage.fireticv2.presentation.home

import android.arch.lifecycle.ViewModel
import com.cabbage.fireticv2.data.FireTicRepository
import timber.log.Timber

class HomeViewModel(private val repository: FireTicRepository) : ViewModel() {

    init {
        Timber.i("init")
    }

    fun firebaseUser() = repository.getFirebaseUser()

    fun signedInUser() = repository.getCurrentUser()

//    fun getGame() = repository.getCurrentGame()
//
//    fun newLocalGame() = repository.createLocalGame()
//
//    fun getGame(gameId: String) = repository.getGame(gameId)
}