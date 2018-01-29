package com.crazyhitty.chdev.ks.news.base

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AppCompatActivity
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

@SuppressLint("Registered")
/**
 * Base class for all the activities in the project. All activities in the project must use this
 * class as parent class.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class BaseAppCompatActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }
}