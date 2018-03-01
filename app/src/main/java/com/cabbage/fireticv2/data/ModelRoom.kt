package com.cabbage.fireticv2.data

import com.google.firebase.firestore.Exclude
import java.util.*

data class ModelRoom(@get:Exclude val roomId: String? = null,
                     val hostPlayerId: String,
                     val guestPlayerId: String? = null,
                     val inviteCode: String) {

    val creationTime = Date().time
}