package com.crazyhitty.chdev.ks.news

import android.app.Application
import uk.co.chrisjenx.calligraphy.CalligraphyConfig



/**
 * Author:      Kartik Sharma
 * Email Id:    cr42yh17m4n@gmail.com
 * Created:     1/25/18 9:47 PM
 * Description: Unavailable
 */
class NewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Google-Medium.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build())
    }
}