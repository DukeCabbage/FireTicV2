package com.cabbage.fireticv2.data

import com.google.firebase.firestore.Exclude
import java.util.*

data class ModelRoom(@get:Exclude val roomId: String? = null,
                     val hostPlayerId: String,
                     val guestPlayerId: String? = null,
                     val inviteCode: String,
                     val gameId: String) {

    val creationTime = Date().time
}

data class ModelGame(@get:Exclude val gameId: String? = null,
                     val roomId: String? = null,
                     val grids: List<Int> = List(81, { 0 }),
                     val lastMove: Triple<Int, Int, Int>? = null)
