package com.crazyhitty.chdev.ks.news.presentation.newslisting

import com.crazyhitty.chdev.ks.news.base.BasePresenter
import com.crazyhitty.chdev.ks.news.base.BaseView
import com.crazyhitty.chdev.ks.news.base.LoadingView

/**
 * Contains blueprint for View and BasePresenter responsible for showing and getting news.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
interface NewsListingContract {

    interface View : BaseView, LoadingView {
        fun render(newsViewModel: NewsViewModel)
    }

    abstract class Presenter : BasePresenter<View>() {
        abstract fun fetchNews()

        abstract fun onNewsItemClicked()
    }
}