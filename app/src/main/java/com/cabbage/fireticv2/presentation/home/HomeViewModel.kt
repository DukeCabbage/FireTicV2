package com.cabbage.fireticv2.presentation.home

import android.arch.lifecycle.ViewModel
import com.cabbage.fireticv2.data.FireTicRepository
import com.cabbage.fireticv2.data.game.ModelGame
import com.cabbage.fireticv2.data.game.ModelMove
import timber.log.Timber

class HomeViewModel(private val repository: FireTicRepository) : ViewModel() {

    init {
        Timber.i("init")
    }

    fun firebaseUser() = repository.getFirebaseUser()

    fun signedInUser() = repository.userRepository.getSignedInUser()

    fun getGame() = repository.getCurrentGame()

    fun makeMove(sector: Int, grid: Int, player: Int) =
            repository.makeMove(sector, grid, player)

    fun newLocalGame() = repository.createLocalGame()

    fun createNewGame() = repository.createGame(ModelGame.online())

    fun getGame(gameId: String) = repository.getGame(gameId)

    fun sthCrazy(sector: Int, grid: Int, player: Int) = repository.sthCrazy(sector, grid, player)
}