package com.cabbage.fireticv2.data.game

import java.util.*

data class ModelGame(val gameId: String = "",
                     val roomId: String? = null,
                     val type: String = "",
                     val hostId: String? = null,
                     val guestId: String? = null,
                     val winner: Int = 0,

                     val lastMove: ModelMove? = null,
                     val grids: List<Int> = List(81, { 0 })) {

    companion object {
        fun online() = ModelGame(type = "online")
        fun local() = ModelGame(gameId = UUID.randomUUID().toString(), type = "local")
    }
}

data class ModelMove(val sectorIndex: Int = 0,
                     val gridIndex: Int = 0,
                     val player: Int = 0,
                     val timestamp: Long = 0L) {

    fun toMap(): Map<String, Any?> = mutableMapOf(
            Pair("sectorIndex", sectorIndex),
            Pair("gridIndex", gridIndex),
            Pair("player", player),
            Pair("timestamp", timestamp)
    )

    companion object {
        fun create(sectorIndex: Int, gridIndex: Int, player: Int) =
                ModelMove(sectorIndex, gridIndex, player, Date().time)
    }
}