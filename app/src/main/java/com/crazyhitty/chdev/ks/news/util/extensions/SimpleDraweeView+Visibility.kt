package com.crazyhitty.chdev.ks.news.util.extensions

import android.net.Uri
import android.view.View
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder

/**
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */

/**
 * Sets the image uri if it is not empty, otherwise hide the simpleDraweeView.
 *
 * @param uriString Uri string for image to be shown.
 */
fun SimpleDraweeView.setImageUriOrHide(autoResize: Boolean, uriString: String?) {
    if (uriString.isNullOrBlank()) {
        visibility = View.GONE
    } else {
        visibility = View.VISIBLE

        // Check if auto resizing is enabled.
        if (autoResize) {
            post {
                val request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uriString))
                        .setResizeOptions(ResizeOptions(measuredWidth.div(2), measuredHeight.div(2)))
                        .setProgressiveRenderingEnabled(true)
                        .build()
                val controller = Fresco.newDraweeControllerBuilder()
                        .setOldController(controller)
                        .setImageRequest(request)
                        .build()
                this.controller = controller
            }
        } else {
            setImageURI(uriString)
        }
    }
}