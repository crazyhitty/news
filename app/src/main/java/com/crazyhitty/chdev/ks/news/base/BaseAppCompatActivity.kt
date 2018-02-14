package com.crazyhitty.chdev.ks.news.base

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.crazyhitty.chdev.ks.news.NewsApplication
import com.crazyhitty.chdev.ks.news.R
import com.crazyhitty.chdev.ks.news.di.components.ActivityComponent
import com.crazyhitty.chdev.ks.news.di.components.DaggerActivityComponent
import com.crazyhitty.chdev.ks.news.di.modules.ActivityModule
import org.jetbrains.anko.find
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

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Set toolbar if available.
        val toolbar = find<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }
}