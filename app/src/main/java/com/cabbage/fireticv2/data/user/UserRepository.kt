package com.cabbage.fireticv2.data.user

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.cabbage.fireticv2.dagger.ApplicationScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber
import javax.inject.Inject

@ApplicationScope
class UserRepository
@Inject constructor(@ApplicationScope firestore: FirebaseFirestore) {

    private val usersCollection = firestore.collection("users")
    private var signedInUserId: String? = null
    private val signedInUser = UserLiveData()
    private val users: MutableMap<String, MutableLiveData<ModelUser>> = HashMap()

    internal fun userAuthorized(user: FirebaseUser?) {
        Timber.v("userAuthorized, uid: ${user?.uid}")

        if (user == null) {
            users.clear()
            signedInUserId = null
            signedInUser.unsetReference()
        } else {
            signedInUserId = user.uid
            signedInUser.setReference(usersCollection.document(user.uid))
        }
    }


    fun getSignedInUser(): LiveData<ModelUser?> {
        return signedInUser
    }

    fun getUser(uid: String): LiveData<ModelUser?> {
        fetchUser(uid)
        return users[uid]!!
    }

    fun createUser(uid: String) {
        if (!users.containsKey(uid)) users += Pair(uid, MutableLiveData())
        val model = ModelUser(userId = uid)
        usersCollection.document(uid)
                .set(model)
                .addOnSuccessListener { users[uid]?.postValue(model) }
                .addOnFailureListener { Timber.e(it) }
    }

    private fun fetchUser(uid: String) {
        if (!users.containsKey(uid)) users += Pair(uid, MutableLiveData())
        usersCollection.document(uid)
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
        usersCollection.document(uid)
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