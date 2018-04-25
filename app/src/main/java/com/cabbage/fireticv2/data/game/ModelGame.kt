package com.cabbage.fireticv2.data.game

import com.google.firebase.firestore.Exclude

//data class ModelGame(val gameId: String = "",
//                     val roomId: String? = null,
//                     val type: String = "",
//                     val hostId: String? = null,
//                     val guestId: String? = null,
//                     val winner: Int = 0,
//
//                     val lastMove: ModelMove? = null,
//                     val grids: List<Int> = List(81, { 0 })) {
//
//    companion object {
//        fun online() = ModelGame(type = "online")
//        fun local() = ModelGame(gameId = UUID.randomUUID().toString(), type = "local")
//    }
//}
//
//data class ModelMove(val sectorIndex: Int = 0,
//                     val gridIndex: Int = 0,
//                     val player: Int = 0,
//                     val timestamp: Long = 0L) {
//
//    fun toMap(): Map<String, Any?> = mutableMapOf(
//            Pair("sectorIndex", sectorIndex),
//            Pair("gridIndex", gridIndex),
//            Pair("player", player),
//            Pair("timestamp", timestamp)
//    )
//
//    companion object {
//        fun create(sectorIndex: Int, gridIndex: Int, player: Int) =
//                ModelMove(sectorIndex, gridIndex, player, Date().time)
//    }
//}

data class Game(val gameId: String = "",
                val type: String = "",
                val hostId: String = "",
                val guestId: String? = null,
                val winner: Int = 0,
                val startTime: Long = 0L,
                val endTime: Long? = null,
                @get:Exclude val moves: List<Move> = emptyList()) {

    override fun toString(): String {
        return "Game(gameId='$gameId', type='$type', hostId='$hostId', guestId=$guestId, winner=$winner, startTime=$startTime, endTime=$endTime)"
    }
}

data class Move(val player: Int = 0,
                val position: Int = -1,
                val timestamp: Long? = null) {

    fun toMap(): Map<String, Any?> = mapOf(
            Pair("position", position),
            Pair("player", player),
            Pair("timestamp", timestamp)
    )
}

data class Game2(val gameId: String = "",
                 val type: String = "",
                 val hostId: String = "",
                 val hostName: String = "",
                 val guestId: String? = null,
                 val guestName: String? = null,
                 val winner: Int = 0,
                 val startTime: Long = 0L,
                 val endTime: Long? = null,
                 val grids: List<Int>? = null,
                 val lastMove: Move? = null) {

    override fun toString(): String {
        return "Game2(gameId='$gameId', type='$type', hostId='$hostId', hostName='$hostName', guestId=$guestId, guestName=$guestName, winner=$winner, startTime=$startTime, endTime=$endTime, lastMove=$lastMove)"
    }

    fun gridsToString(): String {
        return "Game2(gameId='$gameId', grids='$grids')"
    }
}