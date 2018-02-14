package com.crazyhitty.chdev.ks.news

import android.app.Application
import com.crazyhitty.chdev.ks.news.di.components.DaggerApplicationComponent
import com.crazyhitty.chdev.ks.news.di.modules.ApplicationModule
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import javax.inject.Inject

/**
 * Main Application class implementation for this project.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class NewsApplication : Application() {
    val applicationComponent by lazy {
        DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()!!
    }

    @Inject
    lateinit var calligraphyConfig: CalligraphyConfig

    override fun onCreate() {
        super.onCreate()

        // Initialize dagger application component.
        applicationComponent.inject(this)

        // Initialize calligraphy.
        CalligraphyConfig.initDefault(calligraphyConfig)
    }
}