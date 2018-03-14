package com.cabbage.fireticv2.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.cabbage.fireticv2.dagger.ApplicationScope
import com.cabbage.fireticv2.data.game.GameRepository
import com.cabbage.fireticv2.data.user.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
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

}