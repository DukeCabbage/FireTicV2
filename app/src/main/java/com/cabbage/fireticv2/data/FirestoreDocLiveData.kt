package com.cabbage.fireticv2.data

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ListenerRegistration
import timber.log.Timber

internal class FirestoreDocLiveData<T>(private val docRef: DocumentReference,
                                       private val clazz: Class<T>)
    : MutableLiveData<T>() {

    private var listenerRegistration: ListenerRegistration? = null

    override fun onActive() {
        super.onActive()

        listenerRegistration = docRef.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let { Timber.e(it) }
            documentSnapshot?.let { value = it.toObject(clazz) }
        }
    }

    override fun onInactive() {
        super.onInactive()
        listenerRegistration?.remove()
    }
}