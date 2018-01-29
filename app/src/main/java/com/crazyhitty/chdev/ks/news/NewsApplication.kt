package com.crazyhitty.chdev.ks.news

import android.app.Application
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

/**
 * Main Application class implementation for this project.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class NewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize calligraphy.
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Google-Medium.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build())
    }
}