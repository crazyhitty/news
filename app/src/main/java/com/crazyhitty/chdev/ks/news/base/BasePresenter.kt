package com.crazyhitty.chdev.ks.news.base

/**
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
interface BasePresenter<in V : BaseView> {
    fun onAttach(view: V)

    fun onDetach()
}