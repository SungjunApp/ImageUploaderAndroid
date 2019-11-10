package com.sjsoft.app.ui.holders

import android.content.Context
import android.graphics.Point
import android.view.WindowManager

data class MarginInfo(val side: Int
                      , val paddingTopBottom: Int
                      , val paddingLeftRight: Int) {
    fun getRectangleSize(context: Context, span: Int): Int {
        val wm: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)

        val edgeMargin = side * 2
        val sideMargin = paddingLeftRight * 2 * span
        return (size.x - edgeMargin - sideMargin) / span
    }
}