package com.cabbage.fireticv2.data.user

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ListenerRegistration
import timber.log.Timber

@Deprecated("use FirestoreDocLiveData")
class UserLiveData : MutableLiveData<ModelUser>() {

    init {
        value = null
    }

    private var mListenerRegistration: ListenerRegistration? = null
    private var mDocRef: DocumentReference? = null

    fun setReference(docRef: DocumentReference) {
        mDocRef = docRef
        if (hasActiveObservers()) startListening()
    }

    fun unsetReference() {
        mDocRef = null
        mListenerRegistration?.remove()
        value = null
    }

    override fun onActive() {
        if (mDocRef != null) startListening()
    }

    override fun onInactive() {
        mListenerRegistration?.remove()
    }

    private fun startListening() {
        mListenerRegistration = mDocRef?.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let { Timber.e(it) }
            documentSnapshot?.let { snapshot ->
                if (snapshot.exists()) {
                    postValue(snapshot.toObject(ModelUser::class.java))
                } else {
                    postValue(null)
                }
            }
        }
    }
}