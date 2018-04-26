package com.cabbage.fireticv2.presentation.roomlist

import android.arch.lifecycle.ViewModel
import com.cabbage.fireticv2.data.FireTicRepository
import java.util.*

class RoomListViewModel(private val repository: FireTicRepository) : ViewModel() {

    fun availableRooms() = repository.gameRepo.findAvailableRooms()

    fun create() {
        val uid = repository.getFirebaseUser().value?.uid ?: return

        val rnd = Random().nextDouble()
        val private = rnd < 0.3
        val inviteCode = if (private) "1234" else null

        repository.gameRepo.createRoom(uid, private, inviteCode)
    }
}