package com.crazyhitty.chdev.ks.news.base

import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.crazyhitty.chdev.ks.news.NewsApplication
import com.crazyhitty.chdev.ks.news.di.components.ActivityComponent
import com.crazyhitty.chdev.ks.news.di.components.DaggerActivityComponent
import com.crazyhitty.chdev.ks.news.di.modules.ActivityModule
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * Base class for all the activities in the project. All activities in the project must use this
 * class as parent class.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
abstract class BaseAppCompatActivity : AppCompatActivity() {
    protected val activityComponent: ActivityComponent by lazy {
        DaggerActivityComponent.builder()
                .activityModule(ActivityModule(this))
                .applicationComponent((application as NewsApplication).applicationComponent)
                .build()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }
}