package com.cabbage.fireticv2.presentation.utils

import android.animation.Animator
import android.view.View
import timber.log.Timber
import java.lang.ref.WeakReference

open class MyAnimatorListener(protected val wfTargetView: WeakReference<View>) :
        Animator.AnimatorListener {

    companion object {
        private const val logEnabled = false
    }

    override fun onAnimationStart(animator: Animator) {
        wfTargetView.get()?.let {
            if (logEnabled) {
                Timber.v("Anim start with: ")
                printAttributes(it)
            }
        }
    }

    override fun onAnimationEnd(animator: Animator) {
        wfTargetView.get()?.let {
            if (logEnabled) {
                Timber.v("Anim stop with: ")
                printAttributes(it)
            }
        }
    }

    override fun onAnimationCancel(animator: Animator) {}

    override fun onAnimationRepeat(animator: Animator) {}

    private fun printAttributes(view: View) {
        val width = view.width
        val currentScale = view.scaleX
        val x = view.x
        val pivotX = view.pivotX
        val height = view.height
        val elevation = view.elevation

        Timber.v("Width : %d, Scale: %f, x: %f, pivot: %f, height: %d, elevation: %f", width, currentScale, x, pivotX, height, elevation)
    }
}