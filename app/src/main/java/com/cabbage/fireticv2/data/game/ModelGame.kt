package com.cabbage.fireticv2.data.game

data class ModelGame(val gameId: String,
                     val roomId: String? = null,
                     val type: String,
                     val hostId: String? = null,
                     val guestId: String? = null,
                     val grids: List<Int> = List(81, { 0 }),
                     val lastMove: Triple<Int, Int, Int>? = null,
                     val winner: Int = 0) {

    constructor() : this(gameId = "", type = "")
}