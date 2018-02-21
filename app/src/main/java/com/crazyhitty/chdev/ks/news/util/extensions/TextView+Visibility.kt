package com.crazyhitty.chdev.ks.news.util.extensions

import android.view.View
import android.widget.TextView

/**
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
fun TextView.setTextOrHide(value: String?) {
    if (value.isNullOrBlank()) {
        visibility = View.GONE
    } else {
        visibility = View.VISIBLE
        text = value
    }
}