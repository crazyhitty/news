package com.crazyhitty.chdev.ks.news.base

import android.support.annotation.StringRes

/**
 * Every View must implement this interface.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
interface BaseView {
    fun showScreen(action: String, id: String? = null)

    fun replaceScreen(action: String)

    fun showMessage(@StringRes messageResId: Int)
}