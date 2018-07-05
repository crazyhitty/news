package com.crazyhitty.chdev.ks.news.base

import android.content.Context
import android.support.annotation.StringRes
import android.widget.Toast
import com.crazyhitty.chdev.ks.news.util.extensions.showScreenFromAction
import dagger.android.support.DaggerAppCompatActivity
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * Base class for all the activities in the project. All activities in the project must use this
 * class as parent class.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
abstract class BaseActivity : DaggerAppCompatActivity(), BaseView {
    override fun showScreen(action: String, id: String?) {
        showScreenFromAction(action, id)
    }

    override fun replaceScreen(action: String) {
        showScreenFromAction(action)
        finish()
    }

    override fun showMessage(@StringRes messageResId: Int) =
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }
}