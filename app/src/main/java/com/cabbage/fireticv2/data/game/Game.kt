package com.cabbage.fireticv2.data.game

data class Game(val gameId: String = "",
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
        return "Game(gameId='$gameId', type='$type', hostId='$hostId', hostName='$hostName', guestId=$guestId, guestName=$guestName, winner=$winner, startTime=$startTime, endTime=$endTime, lastMove=$lastMove)"
    }

    fun gridsToString(): String {
        return "Game(gameId='$gameId', grids='$grids')"
    }
}