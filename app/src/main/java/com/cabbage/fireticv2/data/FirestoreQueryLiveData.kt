package com.cabbage.fireticv2.data

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import timber.log.Timber

internal class FirestoreQueryLiveData<T>(private val query: Query,
                                         private val clazz: Class<T>)
    : MutableLiveData<List<T>>() {

    private var listenerRegistration: ListenerRegistration? = null

    override fun onActive() {
        super.onActive()

        listenerRegistration = query.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let { Timber.e(it) }
            querySnapshot?.let { value = it.toObjects(clazz) }
        }
    }

    override fun onInactive() {
        super.onInactive()
        listenerRegistration?.remove()
    }
}