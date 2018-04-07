package com.cabbage.fireticv2.presentation.gameboard

import android.support.annotation.LongDef
import timber.log.Timber

const val GridCount = 9
const val SectorCount = GridCount

const val OpenGrid = 0L
const val Player1Token = 1L
const val Player2Token = -1L

@LongDef(OpenGrid, Player1Token, Player2Token)
@Retention(AnnotationRetention.SOURCE)
annotation class Player

private fun checkSame(@Player a: Long, @Player b: Long, @Player c: Long): Boolean {
    return a == b && b == c
}

@Player
fun checkWin(arr: List<Long>): Long {
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