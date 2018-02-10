package com.cabbage.fireticv2.presentation.gameboard

object Order {

    enum class VerticalOrder {
        TOP, CENTER, BOTTOM
    }

    enum class HorizontalOrder {
        LEFT, CENTER, RIGHT
    }

    fun orderToIndex(vo: VerticalOrder, ho: HorizontalOrder): Int {
        val vi = vo.ordinal
        val hi = ho.ordinal
        return vi * HorizontalOrder.values().size + hi
    }

    fun indexToVerticalOrder(index: Int): VerticalOrder {
        val columnCount = HorizontalOrder.values().size
        return VerticalOrder.values()[index / columnCount]
    }

    fun indexToHorizontalOrder(index: Int): HorizontalOrder {
        val columnCount = HorizontalOrder.values().size
        return HorizontalOrder.values()[index % columnCount]
    }
}
