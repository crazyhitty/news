package com.crazyhitty.chdev.ks.news.base

/**
 * Base implementation for presenter. Every presenter in this application should extend this.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
open class Presenter<V : BaseView> : BasePresenter<V> {
    private var _view: V? = null

    val view: V
        get() {
            if (_view == null) {
                throw NullPointerException("The view you are trying to access is null, looks like " +
                        "you forgot to call onAttach(view: V) method.")
            }
            return _view as V
        }

    override fun onAttach(view: V) {
        this._view = view
    }

    override fun onDetach() {
        _view = null
    }
}