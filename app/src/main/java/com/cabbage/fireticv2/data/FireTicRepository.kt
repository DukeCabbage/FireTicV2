package com.cabbage.fireticv2.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.cabbage.fireticv2.dagger.ApplicationScope
import com.cabbage.fireticv2.data.game.Game
import com.cabbage.fireticv2.data.game.GameRepository
import com.cabbage.fireticv2.data.game.Move
import com.cabbage.fireticv2.data.user.ModelUser
import com.cabbage.fireticv2.data.user.UserRepository
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
                    @ApplicationScope val userRepo: UserRepository,
                    @ApplicationScope val gameRepo: GameRepository) {

    fun getFirebaseUser(): LiveData<FirebaseUser> = userRepo.firebaseUser

    fun getCurrentUser(): LiveData<ModelUser> = userRepo.currentUser

    fun createUserForNewUser() {
        if (getFirebaseUser().value == null) {
            Timber.d("No user signed in")
            return
        }


    }

    private val gamesStore = firestore.collection("games")

    fun createOnlineGame(): LiveData<Outcome<String>> {
        val result = MutableLiveData<Outcome<String>>()

        val doc = gamesStore.document()
        val gameId = doc.id

        val user = getCurrentUser().value
        val userId = user?.userId
        if (userId == null) {
            result.value = Outcome.failure(Error("No user id"))
        } else {
            result.value = Outcome.progress(true)

            val newGame = Game(gameId = gameId,
                               type = "online",
                               hostId = userId,
                               hostName = user.name,
                               startTime = Date().time)

            doc.set(newGame).addOnCompleteListener {
                if (it.isSuccessful) {
                    result.value = Outcome.success(gameId)

                    firestore.collection("users").document(userId)
                            .update("currentRoomId", gameId)
                } else {
                    result.value = Outcome.failure(it.exception
                                                   ?: Error("Unknown create game error"))
                }
            }
        }

        return result
    }

    fun startWatching(gameId: String): LiveData<Game> {
        return FirestoreDocLiveData(gamesStore.document(gameId), Game::class.java)
    }

    fun makeMove(game: Game, move: Move): LiveData<Outcome<Move>> {

        val result = MutableLiveData<Outcome<Move>>()
        val newMove = move.copy(timestamp = Date().time)

        result.value = Outcome.progress(true)

        val init: (Int) -> Int = {
            when (it) {
                newMove.position -> newMove.player
                else -> game.grids?.get(it) ?: 0
            }
        }
        val newGrids = List(81, init)

//        val updated = game.copy(grids = newGrids, lastMove = newMove)

        gamesStore.document(game.gameId)
                .update("grids", newGrids, "lastMove", newMove.toMap())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        result.value = Outcome.success(newMove)
                    } else {
                        result.value = Outcome.failure(it.exception ?: Error())
                    }
                }

        return result
    }
}