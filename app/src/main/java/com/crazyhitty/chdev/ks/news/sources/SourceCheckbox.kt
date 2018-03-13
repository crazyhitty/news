package com.crazyhitty.chdev.ks.news.sources

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import com.crazyhitty.chdev.ks.news.R
import com.crazyhitty.chdev.ks.news.R.attr.checkedTrueDrawable

/**
 * Implementation of a simple check box using an ImageView.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class SourceCheckbox : ImageView {
    var checkedTrueDrawable: Drawable? = null
    var checkedFalseDrawable: Drawable? = null

    var checked: Boolean = false
        set(value) {
            field = value

            checkForImageAvailability()

            if (value) {
                setImageDrawable(checkedTrueDrawable)
            } else {
                setImageDrawable(checkedFalseDrawable)
            }
        }

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        // Make the view clickable.
        isClickable = true

        val typedArray = context.theme.obtainStyledAttributes(attrs,
                R.styleable.SourceCheckbox,
                0,
                0)

        try {
            checkedTrueDrawable = typedArray.getDrawable(R.styleable.SourceCheckbox_checkedTrueDrawable)
            checkedFalseDrawable = typedArray.getDrawable(R.styleable.SourceCheckbox_checkedFalseDrawable)
            checked = typedArray.getBoolean(R.styleable.SourceCheckbox_checked, false)
        } finally {
            typedArray.recycle()
        }

        checkForImageAvailability()

        setOnClickListener { checked = !checked }
    }

    /**
     * Checks if [checkedTrueDrawable] and [checkedFalseDrawable] are provided or not. If any of
     * these drawables are not provided then throw exception.
     */
    private fun checkForImageAvailability() {
        if (checkedTrueDrawable == null) throw NullPointerException("Please provide checkedTrueDrawable")
        if (checkedFalseDrawable == null) throw NullPointerException("Please provide checkedFalseDrawable")
    }
}