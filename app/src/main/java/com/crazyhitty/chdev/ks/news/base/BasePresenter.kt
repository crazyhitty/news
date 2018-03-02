package com.crazyhitty.chdev.ks.news.base

/**
 * Every presenter should implement this presenter.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
interface BasePresenter<in V : BaseView> {
    /**
     * Called when a view is attached with this presenter.
     *
     * @param view  The view with which this presenter would interact with.
     */
    fun onAttach(view: V)

    /**
     * Called when a view is detached from this presenter. You should also release any resources
     * held by this presenter in this method's body.
     */
    fun onDetach()
}