package com.cabbage.fireticv2.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.cabbage.fireticv2.dagger.ApplicationScope
import com.cabbage.fireticv2.data.game.GameRepository
import com.cabbage.fireticv2.data.game.ModelGame
import com.cabbage.fireticv2.data.user.UserRepository
import com.cabbage.fireticv2.presentation.gameboard.GridCount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@ApplicationScope
class FireTicRepository
@Inject constructor(@ApplicationScope val auth: FirebaseAuth,
                    @ApplicationScope private val firestore: FirebaseFirestore,
                    @ApplicationScope val userRepository: UserRepository,
                    @ApplicationScope val gameRepository: GameRepository) {

    private val firebaseUser = MutableLiveData<FirebaseUser?>()

    init {
        firebaseUser.observeForever { userRepository.userAuthorized(it) }
        auth.addAuthStateListener { firebaseUser.postValue(it.currentUser) }
    }

    fun getFirebaseUser(): LiveData<FirebaseUser?> = firebaseUser

    //region Game

    private val currentGame = MutableLiveData<ModelGame>()

    fun getCurrentGame(): LiveData<ModelGame> {
        if (currentGame.value == null) {
            Timber.i("Create default game")
            createLocalGame()
        }
        return currentGame
    }

    fun createLocalGame() {
        currentGame.value = ModelGame(gameId = UUID.randomUUID().toString(), type = "local")
    }

    fun makeMove(sector: Int, grid: Int, player: Int) {
        Timber.v("$sector, $grid, $player")
        val match = sector * GridCount + grid
        val model = currentGame.value!!

        if (model.grids[match] != 0) {
            Timber.w("Already set, do nothing")
            return
        }

        when (model.type) {
            "local" -> {
                val grids = model.grids.mapIndexed { index, item ->
                    when (index) {
                        match -> player
                        else -> item
                    }
                }
                val lastMove = Triple(sector, grid, player)

                currentGame.value = model.copy(grids = grids, lastMove = lastMove)
            }
            else -> throw NotImplementedError()
        }
    }

    //endregion
}