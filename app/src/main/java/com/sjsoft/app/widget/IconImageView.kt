package com.sjsoft.app.widget

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import android.graphics.PorterDuff
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import com.sjsoft.app.R


class IconImageView : AppCompatImageView {
    constructor(context: Context) : super(context, null) {
        //initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setAttr(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        setAttr(context, attrs)
    }

    var imageColor = 0
        set(value) {
            field = value
            setColorFilter(imageColor, PorterDuff.Mode.MULTIPLY)
        }

    fun setAttr(context: Context, attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.IconImageView)
        imageColor =
            ta.getColor(R.styleable.IconImageView_imageColor, ContextCompat.getColor(context, android.R.color.black))
        this.imageColor = imageColor
        ta.recycle()
    }

}