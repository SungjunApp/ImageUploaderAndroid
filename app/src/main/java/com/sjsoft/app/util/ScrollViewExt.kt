package com.sjsoft.app.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView

internal val offSetRange = 150f
fun View.changeAlphaForScroll(scrollOffSet: Float) {
    alpha = if (scrollOffSet in 0.0f..offSetRange) {
        scrollOffSet / offSetRange
    } else if (scrollOffSet > offSetRange) {
        1f
    } else {
        0f
    }

}

fun RecyclerView.setShadowViewController(shadowView: View): RecyclerView.OnScrollListener {
    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            shadowView.changeAlphaForScroll(recyclerView.computeVerticalScrollOffset().toFloat())
        }
    }
    addOnScrollListener(scrollListener)
    return scrollListener
}

