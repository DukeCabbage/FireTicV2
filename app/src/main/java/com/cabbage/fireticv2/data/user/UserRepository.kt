package com.cabbage.fireticv2.data.user

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.cabbage.fireticv2.dagger.ApplicationScope
import com.cabbage.fireticv2.data.MutableLiveDocument
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber
import javax.inject.Inject

@ApplicationScope
class UserRepository
@Inject constructor(@ApplicationScope auth: FirebaseAuth,
                    @ApplicationScope firestore: FirebaseFirestore) {

    private val usersStore = firestore.collection("users")

    private val users: MutableMap<String, MutableLiveData<ModelUser>> = HashMap()

    internal val firebaseUser = MutableLiveData<FirebaseUser>()
    internal val currentUser = MutableLiveDocument(ModelUser::class.java)

    init {
        auth.addAuthStateListener {
            val user = it.currentUser
            Timber.i("FirebaseAuth state change, uid: ${user?.uid}")

            firebaseUser.postValue(user)
            if (user == null) {
                currentUser.document = null
            } else {
                currentUser.document = usersStore.document(user.uid)
            }
        }
    }

    fun getUser(uid: String): LiveData<ModelUser?> {
        fetchUser(uid)
        return users[uid]!!
    }

    fun createUser(uid: String) {
        if (!users.containsKey(uid)) users += Pair(uid, MutableLiveData())
        val model = ModelUser(userId = uid)
        usersStore.document(uid)
                .set(model)
                .addOnSuccessListener { users[uid]?.postValue(model) }
                .addOnFailureListener { Timber.e(it) }
    }

    private fun fetchUser(uid: String) {
        if (!users.containsKey(uid)) users += Pair(uid, MutableLiveData())
        usersStore.document(uid)
                .get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        val model = it.toObject(ModelUser::class.java)
                        users[uid]?.postValue(model)
                    } else {
                        users[uid]?.postValue(null)
                    }
                }
                .addOnFailureListener { Timber.e(it) }
    }

    fun updateUserName(uid: String, newName: String) {
        usersStore.document(uid)
                .update("name", newName)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Timber.v("updateUserName success, uid: $uid, name: $newName")

                        val newModel = users[uid]?.value?.copy(name = newName)
                        if (newModel != null) users[uid]?.postValue(newModel)

                    } else {
                        Timber.e(it.exception)
                    }
                }
    }
}