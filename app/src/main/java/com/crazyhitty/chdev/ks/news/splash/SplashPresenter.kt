package com.crazyhitty.chdev.ks.news.splash

import com.crazyhitty.chdev.ks.news.base.Presenter
import com.crazyhitty.chdev.ks.news.data.DataStore
import javax.inject.Inject

/**
 * Implementation of [SplashContract.Presenter]. The main job of this presenter is to check if user
 * should be redirected to sources selection screen or news listing screen.
 *
 * @param dataStore [DataStore]'s instance for caching sources so it can be used later.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class SplashPresenter @Inject constructor(private val dataStore: DataStore) : Presenter<SplashContract.View>(), SplashContract.Presenter {

}