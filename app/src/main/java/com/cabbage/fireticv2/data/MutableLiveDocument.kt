package com.cabbage.fireticv2.data

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ListenerRegistration
import timber.log.Timber

class MutableLiveDocument<T>(private val clazz: Class<T>,
                             document: DocumentReference? = null)
    : MutableLiveData<T>() {

    private var registration: ListenerRegistration? = null

    var document: DocumentReference? = document
        set(it) {
            if (it == null) {
                registration?.remove()
                postValue(null)
            } else {
                startListener()
            }
            field = it
        }

    private fun startListener() {
        registration?.remove()
        if (!hasActiveObservers()) return

        registration = document?.addSnapshotListener { snapshot, exception ->
            exception?.let { Timber.w(it) }

            snapshot?.let {
                if (it.exists()) {
                    postValue(it.toObject(clazz))
                } else {
                    postValue(null)
                }
            }
        }

    }

    override fun onActive() {
        startListener()
    }

    override fun onInactive() {
        registration?.remove()
    }
}