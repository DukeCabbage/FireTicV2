package com.cabbage.fireticv2.data.game

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.cabbage.fireticv2.dagger.ApplicationScope
import com.cabbage.fireticv2.data.FirestoreQueryLiveData
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@ApplicationScope
class GameRepository
@Inject constructor(firestore: FirebaseFirestore) {

    private val roomsCollection = firestore.collection("rooms")
    private val gamesCollection = firestore.collection("games")

    fun createRoom(creatorUId: String,
                   private: Boolean,
                   inviteCode: String? = null)
            : LiveData<ModelRoom> {

        val roomId = roomsCollection.document().id
        val liveData = data<ModelRoom>()

        val model = ModelRoom(roomId = roomId,
                              hostPlayerId = creatorUId,
                              creationTime = Date().time,
                              privateRoom = private,
                              inviteCode = inviteCode)


        roomsCollection.document(roomId)
                .set(model)
                .addOnSuccessListener { liveData.postValue(model) }
                .addOnFailureListener { Timber.e(it) }

        return liveData
    }

    fun getRoom(roomId: String): LiveData<ModelRoom> {

        val liveData = data<ModelRoom>()

        roomsCollection.document(roomId)
                .get()
                .addOnSuccessListener { liveData.postValue(it.toObject(ModelRoom::class.java)) }
                .addOnFailureListener { Timber.e(it) }

        return liveData
    }

    fun findAvailableRooms(): LiveData<List<ModelRoom>> {
        val query = roomsCollection.whereEqualTo("progress", ModelRoom.Empty)
//                .whereEqualTo("privateRoom", false)

        return FirestoreQueryLiveData(query, ModelRoom::class.java)
    }

    private inline fun <reified T> data() = MutableLiveData<T>()
}