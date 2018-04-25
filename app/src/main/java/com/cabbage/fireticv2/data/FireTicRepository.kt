package com.cabbage.fireticv2.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.cabbage.fireticv2.dagger.ApplicationScope
import com.cabbage.fireticv2.data.game.*
import com.cabbage.fireticv2.data.user.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import javax.inject.Inject

@ApplicationScope
class FireTicRepository
@Inject constructor(@ApplicationScope val auth: FirebaseAuth,
                    @ApplicationScope private val firestore: FirebaseFirestore,
                    @ApplicationScope val userRepository: UserRepository,
                    @ApplicationScope val gameRepository: GameRepository) {

    private val firebaseUser = MutableLiveData<FirebaseUser?>()

    private val gamesStore = firestore.collection("games")

    init {
        firebaseUser.observeForever { userRepository.userAuthorized(it) }
        auth.addAuthStateListener { firebaseUser.postValue(it.currentUser) }
    }

    fun getFirebaseUser(): LiveData<FirebaseUser?> = firebaseUser

    //region Game

//    private val currentGame = MutableLiveData<ModelGame>()
//
//    fun getCurrentGame(): LiveData<ModelGame> {
//        if (currentGame.value == null) {
//            Timber.i("Create default game")
//            createLocalGame()
//        }
//        return currentGame
//    }
//
//    fun createLocalGame() {
//        currentGame.value = ModelGame(gameId = UUID.randomUUID().toString(), type = "local")
//    }
//
//    fun makeMove(sector: Int, grid: Int, player: Int) {
//        Timber.v("$sector, $grid, $player")
//        val match = sector * GridCount + grid
//        val model = currentGame.value!!
//
//        if (model.grids[match] != 0) {
//            Timber.w("Already set, do nothing")
//            return
//        }
//
//        when (model.type) {
//            "local" -> {
//                val grids = model.grids.mapIndexed { index, item ->
//                    when (index) {
//                        match -> player
//                        else -> item
//                    }
//                }
//                val lastMove = ModelMove.create(sector, grid, player)
//
//                currentGame.value = model.copy(grids = grids, lastMove = lastMove)
//            }
//            else -> throw NotImplementedError()
//        }
//    }
//
//    fun getGame(gameId: String): LiveData<Outcome<ModelGame>> {
//
//        val result = object : MutableLiveData<Outcome<ModelGame>>() {
//            private var listenerRegistration: ListenerRegistration? = null
//
//            override fun onInactive() {
//                listenerRegistration?.remove()
//            }
//
//            override fun onActive() {
//                super.onActive()
//                listenerRegistration = firestore.collection("games")
//                        .document(gameId)
//                        .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
//                            firebaseFirestoreException?.let { this.value = Outcome.failure(it) }
//                            documentSnapshot?.let {
//                                if (it.exists()) {
//                                    this.value = Outcome.success(it.toObject(ModelGame::class.java))
//                                } else {
//                                    this.value = Outcome.failure(IllegalStateException("Game $gameId does not exist"))
//                                }
//                            }
//                        }
//            }
//        }
//
//        result.value = Outcome.progress(true)
//
//        return result
//    }

    //endregion

    fun createOnlineGame(): LiveData<Outcome<String>> {
        val newDocRef = firestore.collection("games").document()
        val gameId = newDocRef.id
        val userId = userRepository.getSignedInUser().value?.userId
        val result = MutableLiveData<Outcome<String>>()

        if (userId != null) {
            result.value = Outcome.progress(true)

            val newGame = Game(gameId = gameId, hostId = userId, startTime = Date().time, type = "online")

            newDocRef.set(newGame)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            result.value = Outcome.success(gameId)

                            firestore.collection("users").document(userId)
                                    .update("currentRoomId", gameId)
                        } else {
                            result.value = Outcome.failure(it.exception ?: Error())
                        }
                    }

        } else {
            result.value = Outcome.failure(Error("No user id"))
        }

        return result
    }

    fun startWatching(gameId: String): LiveData<Game> {
        return LiveGameData(firestore, gameId)
    }

    fun makeMove(game: Game, move: Move): LiveData<Outcome<Move>> {

        val result = MutableLiveData<Outcome<Move>>()
        val todo = move.copy(timestamp = Date().time)

        result.value = Outcome.progress(true)

        firestore.collection("games")
                .document(game.gameId)
                .collection("moves")
                .add(todo)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        result.value = Outcome.success(todo)
                    } else {
                        result.value = Outcome.failure(it.exception ?: Error())
                    }
                }

        return result
    }


    // Another try

    fun createOnlineGame2(): LiveData<Outcome<String>> {
        val result = MutableLiveData<Outcome<String>>()

        val doc = gamesStore.document()
        val gameId = doc.id

        val user = userRepository.getSignedInUser().value
        val userId = user?.userId
        if (userId == null) {
            result.value = Outcome.failure(Error("No user id"))
        } else {
            result.value = Outcome.progress(true)

            val newGame = Game2(gameId = gameId,
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

    fun startWatching2(gameId: String): LiveData<Game2> {
        return FirestoreDocLiveData(gamesStore.document(gameId), Game2::class.java)
    }

    fun makeMove2(game: Game2, move: Move): LiveData<Outcome<Move>> {

        val result = MutableLiveData<Outcome<Move>>()
        val newMove = move.copy(timestamp = Date().time)

        result.value = Outcome.progress(true)

        val newGrids = List(size = 81, init = { i ->
            when (i) {
                newMove.position -> newMove.player
                else -> game.grids?.get(i) ?: 0
            }
        })

        val updated = game.copy(grids = newGrids, lastMove = newMove)

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