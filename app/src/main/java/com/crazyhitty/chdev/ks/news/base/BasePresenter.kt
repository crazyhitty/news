package com.crazyhitty.chdev.ks.news.base

/**
 * Base implementation for presenter. Every presenter in this application should extend this.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
abstract class BasePresenter<V : BaseView> {

    open fun onAttach() {
    }

    open fun onDetach() {
    }
}