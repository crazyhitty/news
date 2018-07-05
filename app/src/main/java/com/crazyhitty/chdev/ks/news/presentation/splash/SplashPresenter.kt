package com.crazyhitty.chdev.ks.news.presentation.splash

import javax.inject.Inject

/**
 * Implementation of [SplashContract.Presenter]. The main job of this presenter is to check if user
 * should be redirected to sources selection screen or news listing screen.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class SplashPresenter @Inject constructor(
        private val view: SplashContract.View
) : SplashContract.Presenter() {

}