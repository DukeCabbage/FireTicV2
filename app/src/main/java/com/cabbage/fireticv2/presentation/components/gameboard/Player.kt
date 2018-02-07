package com.cabbage.fireticv2.presentation.components.gameboard

enum class Player(val token: Int) {
    One(1) {
        override fun toggle() = Two
    },
    Two(-1) {
        override fun toggle() = One
    },
    Open(0);

    open fun toggle(): Player = Open
    open fun reset(): Player = Open

    companion object {
        fun playerBy(token: Int): Player {
            return when(token) {
                Player.One.token -> Player.One
                Player.Two.token -> Player.Two
                else -> Player.Open
            }
        }
    }
}