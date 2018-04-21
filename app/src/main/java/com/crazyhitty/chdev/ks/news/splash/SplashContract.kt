package com.crazyhitty.chdev.ks.news.splash

import com.crazyhitty.chdev.ks.news.base.BasePresenter
import com.crazyhitty.chdev.ks.news.base.BaseView

/**
 * Contains blueprint for View and Presenter responsible for managing splash screen.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
interface SplashContract {
    interface View : BaseView {
        /**
         * Redirect user to sources activity.
         */
        fun redirectToSourcesActivity()

        /**
         * Redirect user to news activity.
         */
        fun redirectToNewsActivity()
    }

    interface Presenter : BasePresenter<View>
}