package com.cabbage.fireticv2.presentation.components.gameboard

import android.animation.Animator
import android.view.View
import java.lang.ref.WeakReference

/**
 * A class that listens on animation start and end event,
 * then sets elevation on the animated view
 */
class ElevationSetter(wfView: WeakReference<View>,
                      private val defaultElevation: Float)
    : MyAnimatorListener(wfView) {
    override fun onAnimationStart(animator: Animator) {
        wfTargetView.get()?.let { view ->
            // Increases elevation if scale starts at 1,
            // which means it's becoming active
            if (view.scaleX.toDouble() == 1.0) {
                view.elevation = 2.0f * defaultElevation
            }
        }
        super.onAnimationStart(animator)
    }

    override fun onAnimationEnd(animator: Animator) {
        wfTargetView.get()?.let { view ->
            // Decreases elevation to default if scale ends at 1,
            // which means it has returned back to normal
            if (view.scaleX.toDouble() == 1.0) {
                view.elevation = defaultElevation
            }
        }
        super.onAnimationEnd(animator)
    }
}