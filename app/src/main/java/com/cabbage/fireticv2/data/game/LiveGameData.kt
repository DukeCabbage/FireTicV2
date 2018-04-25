package com.cabbage.fireticv2.data.game

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import timber.log.Timber

internal class LiveGameData(firestore: FirebaseFirestore,
                            gameId: String)
    : MutableLiveData<Game>() {

    private val doc = firestore.collection("games").document(gameId)

    private var docListener: ListenerRegistration? = null
    private var movesListener: ListenerRegistration? = null

    override fun onActive() {
        super.onActive()

        docListener = doc.addSnapshotListener { snapshot, exception ->
            exception?.let { Timber.w(it) }

            if (snapshot != null && snapshot.exists()) {
                val game = snapshot.toObject(Game::class.java)
                value = game.copy(moves = value?.moves ?: emptyList())

                if (movesListener == null) watchMoves()
            }
        }
    }

    private fun watchMoves() {
        movesListener = doc.collection("moves").addSnapshotListener { snapshot, exception ->
            exception?.let { Timber.w(it) }

            if (snapshot != null && !snapshot.isEmpty) {
                val moves = snapshot.toObjects(Move::class.java)
                value?.let { value = it.copy(moves = moves) }
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        docListener?.remove()
        movesListener?.remove()
    }
}