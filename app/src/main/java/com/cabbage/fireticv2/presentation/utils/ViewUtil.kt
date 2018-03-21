package com.cabbage.fireticv2.presentation.utils

import android.content.Context
import android.widget.Toast


@Suppress("unused")
object ViewUtil {
    fun getStatusBarHeight(context: Context): Int? {
        val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resId > 0) context.resources.getDimensionPixelOffset(resId)
        else null
    }

    fun dpToPixel(context: Context, dp: Int): Float {
        val metrics = context.resources.displayMetrics
        return dp * (metrics.densityDpi / 160f)
    }
}

fun Context.toast(messageResId: Int, duration: Int = Toast.LENGTH_LONG) =
        Toast.makeText(this, messageResId, duration).show()

fun Context.toast(message: String, duration: Int = Toast.LENGTH_LONG) =
        Toast.makeText(this, message, duration).show()