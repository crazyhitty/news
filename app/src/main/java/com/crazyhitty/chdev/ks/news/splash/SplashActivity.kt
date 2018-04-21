package com.crazyhitty.chdev.ks.news.splash

import android.os.Bundle
import com.crazyhitty.chdev.ks.news.base.BaseAppCompatActivity

/**
 * This activity will display when the app is just started.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class SplashActivity : BaseAppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize dependency.
        activityComponent.inject(this)
    }
}