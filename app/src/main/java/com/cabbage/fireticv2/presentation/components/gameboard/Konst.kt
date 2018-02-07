package com.cabbage.fireticv2.presentation.components.gameboard

import timber.log.Timber

object Konst {

    const val BoardCount = 9
    const val GridCount = BoardCount

    const val OpenGrid = 0
    const val Player1Token = 1
    const val Player2Token = -1

    const val Invalid = -99
    const val NotChosen = -67

    private fun checkSame(a: Int, b: Int, c: Int): Boolean {
        return a == b && b == c
    }

    fun checkWin(arr: List<Int>): Int {
        // Diagonals
        val center = arr[4]
        if (center != OpenGrid) {
            if (checkSame(arr[0], center, arr[8]) || checkSame(arr[2], center, arr[6])) {
                Timber.v("Diagonal, winner: $center")
                return center
            }
        }

        // Rows
        for (i in listOf(0, 3, 6)) {
            if (arr[i] != OpenGrid && checkSame(arr[i], arr[i + 1], arr[i + 2])) {
                Timber.v("Row, winner: ${arr[i]}")
                return arr[i]
            }
        }

        // Columns
        for (j in listOf(0, 1, 2)) {
            if (arr[j] != OpenGrid && checkSame(arr[j], arr[j + 3], arr[j + 6])) {
                Timber.v("Column, winner: ${arr[j]}")
                return arr[j]
            }
        }

        Timber.v("No winner")

        return OpenGrid
    }
}
