package com.crazyhitty.chdev.ks.news.util.extensions

import android.view.View
import com.facebook.drawee.view.SimpleDraweeView

/**
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */

/**
 * Sets the image uri if it is not empty, otherwise hide the simpleDraweeView.
 *
 * @param uriString Uri string for image to be shown.
 */
fun SimpleDraweeView.setImageUriOrHide(uriString: String?) {
    if (uriString.isNullOrBlank()) {
        visibility = View.GONE
    } else {
        visibility = View.VISIBLE
        setImageURI(uriString)
    }
}