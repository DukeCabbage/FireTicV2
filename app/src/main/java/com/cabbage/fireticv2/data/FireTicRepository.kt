package com.cabbage.fireticv2.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.cabbage.fireticv2.dagger.ApplicationScope
import com.cabbage.fireticv2.data.game.GameRepository
import com.cabbage.fireticv2.data.game.ModelGame
import com.cabbage.fireticv2.data.game.ModelMove
import com.cabbage.fireticv2.data.user.UserRepository
import com.cabbage.fireticv2.presentation.gameboard.GridCount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
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
                val lastMove = ModelMove.create(sector, grid, player)

                currentGame.value = model.copy(grids = grids, lastMove = lastMove)
            }
            else -> throw NotImplementedError()
        }
    }

    //endregion


    fun createGame(data: ModelGame): LiveData<Outcome<ModelGame>> {

        val newDocRef = firestore.collection("games").document()
        val gameId = newDocRef.id
        val result = object : MutableLiveData<Outcome<ModelGame>>() {
            override fun onActive() {
                super.onActive()
                firestore.collection("games").document(gameId)
                        .set(data.copy(gameId = gameId))
                        .addOnSuccessListener {
                            newDocRef.get()
                                    .addOnSuccessListener { this.value = Outcome.success(it.toObject(ModelGame::class.java)) }
                                    .addOnFailureListener { this.value = Outcome.failure(it) }
                        }
                        .addOnFailureListener { this.value = Outcome.failure(it) }
            }
        }

        result.value = Outcome.progress(true)

        return result
    }

    fun getGame(gameId: String): LiveData<Outcome<ModelGame>> {

        val result = object : MutableLiveData<Outcome<ModelGame>>() {
            private var listenerRegistration: ListenerRegistration? = null

            override fun onInactive() {
                listenerRegistration?.remove()
            }

            override fun onActive() {
                super.onActive()
                listenerRegistration = firestore.collection("games")
                        .document(gameId)
                        .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                            firebaseFirestoreException?.let { this.value = Outcome.failure(it) }
                            documentSnapshot?.let {
                                if (it.exists()) {
                                    this.value = Outcome.success(it.toObject(ModelGame::class.java))
                                } else {
                                    this.value = Outcome.failure(IllegalStateException("Game $gameId does not exist"))
                                }
                            }
                        }
            }
        }

        result.value = Outcome.progress(true)

        return result
    }

    fun sthCrazy(sector: Int, grid: Int, player: Int) {
        val gameId = "rOqLbgPOQwYbHqDzbdG1"
        val dataObj = ModelMove.create(sector, grid, player).toMap()

        val index = sector * 9 + grid
//        val grids: MutableList<Int> = List(81, { i -> if (index == i) player else 0 })
        val grids = IntArray(8)

        IntArray(8, { 0 })

//        val grids = List(81, { i -> if (i == 0) 1 else null })
//        val grids = List(81, { 0 })

        firestore.collection("games").document(gameId)
                .update("lastMove", dataObj, "grids", grids)
                .addOnSuccessListener { Timber.w("Update success") }
                .addOnFailureListener { Timber.w(it) }
    }
}