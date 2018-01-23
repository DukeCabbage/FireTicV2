package com.cabbage.fireticv2

import android.content.Context


@Suppress("unused")
object ViewUtil {
    fun getStatusBarHeight(context: Context): Int {
        val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resId > 0) context.resources.getDimensionPixelOffset(resId)
        else 0
    }

    fun dpToPixel(context: Context, dp: Int) :Float {
        val metrics = context.resources.displayMetrics
        return dp * (metrics.densityDpi / 160f)
    }
}