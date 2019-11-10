package com.sjsoft.app.util

import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager

fun ConstraintSet.setupLoadMore(
    parentView: ConstraintLayout,
    childView: View,
    showLoadMore: Boolean
) {
    clone(parentView)

    if (showLoadMore) {
        clear(childView.id, ConstraintSet.TOP)
        connect(
            childView.id, ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID, ConstraintSet.TOP
        )
        connect(
            childView.id, ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM
        )
    } else {
        connect(
            childView.id, ConstraintSet.TOP,
            ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM
        )
        clear(childView.id, ConstraintSet.BOTTOM)
    }

    val changeBounds = ChangeBounds()
    changeBounds.duration = 350
    changeBounds.interpolator = AnticipateOvershootInterpolator(1.0f)
    TransitionManager.beginDelayedTransition(parentView, changeBounds)

    applyTo(parentView)
}