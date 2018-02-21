package com.crazyhitty.chdev.ks.news.newsListing

import android.os.Bundle
import com.crazyhitty.chdev.ks.news.base.BasePresenter
import com.crazyhitty.chdev.ks.news.base.BaseView
import com.crazyhitty.chdev.ks.news.data.api.model.news.ArticlesItem
import com.crazyhitty.chdev.ks.news.data.api.model.news.News

/**
 * Contains blueprint for View and Presenter responsible for showing and getting news.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
interface NewsListingContract {

    interface View : BaseView {
        /**
         * Enable refresh UI, so that user can refresh the news.
         */
        fun enableRefresh()

        /**
         * Disallow the refresh UI to trigger.
         */
        fun disableRefresh()

        /**
         * Show the progress indicating loading of news.
         */
        fun showProgress()

        /**
         * Hide the progress indicating loading of news.
         */
        fun hideProgress()

        /**
         * Show news on the UI.
         *
         * @param news  Contains all of the news available
         */
        fun showNews(news: News)

        /**
         * Open the activity responsible for showing news details.
         *
         * @param bundle    Bundle containing any extra details which will be required by the
         *                  receiving activity
         */
        fun openNewsDetailsActivity(bundle: Bundle)

        /**
         * Clear the news currently being displayed.
         */
        fun clearNews()

        /**
         * Stop refreshing.
         */
        fun stopRefreshing()

        /**
         * Show message to UI when refreshing is done.
         */
        fun showRefreshingDoneMessage(message: String)

        /**
         * Hide error message being shown on the screen.
         */
        fun hideError()

        /**
         * Show error message if something goes wrong.
         */
        fun showError(message: String)

        /**
         * Show error toast if something goes wrong.
         */
        fun showErrorToast(message: String)
    }

    interface Presenter: BasePresenter<View> {
        /**
         * Refreshes the news.
         */
        fun refresh()

        /**
         * Redirect the user to news details screen.
         *
         * @param bundle        Provide a bundle which will be returned via
         *                      [View.openNewsDetailsActivity] later on with extra details which
         *                      you can transport to News details screen.
         * @param articlesItem  Extra news information
         */
        fun redirectToNewsDetailsScreen(bundle: Bundle, articlesItem: ArticlesItem?)
    }
}