package com.crazyhitty.chdev.ks.news.util.extensions

import android.view.View
import android.widget.TextView

/**
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */

/**
 * Sets the text if it is not empty, otherwise hide the textView.
 *
 * @param text Value that you would like to set for the textView.
 */
fun TextView.setTextOrHide(text: String?) {
    if (text.isNullOrBlank()) {
        visibility = View.GONE
    } else {
        visibility = View.VISIBLE
        this.text = text
    }
}