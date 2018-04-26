package com.cabbage.fireticv2.data.game

data class Move(val player: Int = 0,
                val position: Int = -1,
                val timestamp: Long? = null) {

    fun toMap(): Map<String, Any?> = mapOf(
            Pair("position", position),
            Pair("player", player),
            Pair("timestamp", timestamp)
    )
}