package com.cabbage.fireticv2.data.game

data class ModelGame(val gameId: String = "",
                     val roomId: String = "",
                     val grids: List<Int> = List(81, { 0 }),
                     val lastMove: Triple<Int, Int, Int>? = null)
