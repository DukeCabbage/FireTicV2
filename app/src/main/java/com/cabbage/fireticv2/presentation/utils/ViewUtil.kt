package com.cabbage.fireticv2.presentation.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.graphics.Point
import android.support.v4.app.Fragment
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

val Activity.isLandscape: Boolean
    get() = resources.configuration.orientation == ORIENTATION_LANDSCAPE


val Fragment.isLandscape: Boolean?
    get() = activity?.isLandscape

fun Context.toast(messageResId: Int, duration: Int = Toast.LENGTH_LONG) =
        Toast.makeText(this, messageResId, duration).show()

fun Context.toast(message: String, duration: Int = Toast.LENGTH_LONG) =
        Toast.makeText(this, message, duration).show()

// Reminder: https://kotlinlang.org/docs/reference/extensions.html#extensions-are-resolved-statically
val Activity?.windowWidth: Int
    get() {
        val size = Point()
        this?.windowManager?.defaultDisplay?.getSize(size)
        return size.x
    }

val Activity?.windowHeight: Int
    get() {
        val size = Point()
        this?.windowManager?.defaultDisplay?.getSize(size)
        return size.y
    }