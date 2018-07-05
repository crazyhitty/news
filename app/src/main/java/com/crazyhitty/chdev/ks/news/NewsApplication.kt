package com.crazyhitty.chdev.ks.news

import com.crazyhitty.chdev.ks.news.di.components.DaggerApplicationComponent
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.stetho.Stetho
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

/**
 * Main Application class implementation for this project.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class NewsApplication : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()

        // Initialize calligraphy.
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/WorkSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build())

        // Initialize fresco.
        Fresco.initialize(this)

        // Initialize Stetho.
        Stetho.initializeWithDefaults(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerApplicationComponent.builder().application(this).build()
        appComponent.inject(this)
        return appComponent
    }
}