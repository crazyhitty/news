package com.crazyhitty.chdev.ks.news.newsListing

import android.os.Bundle
import com.crazyhitty.chdev.ks.news.base.BasePresenter
import com.crazyhitty.chdev.ks.news.base.BaseView
import com.crazyhitty.chdev.ks.news.data.api.model.news.ArticlesItem

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
         * Show news articles on the UI.
         *
         * @param articles  Contains all of the news available
         */
        fun showNewsArticles(articles: ArrayList<ArticlesItem?>)

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

        /**
         * Show error on last item of recycler view instead of showing loading view.
         */
        fun showRecyclerLoadMoreErrorView(message: String)

        /**
         * Show recycler loading view. This will automatically hide load more error view if it is
         * being shown.
         */
        fun showRecyclerLoadingView()

        /**
         * Start listening if last 5th news item is being shown on the screen or not.
         */
        fun startListeningForLastFifthNewsItemShown()

        /**
         * Stop listening if last 5th news item is being shown on the screen or not.
         */
        fun stopListeningForLastFifthNewsItemShown()
    }

    interface Presenter: BasePresenter<View> {
        /**
         * Refreshes the news.
         */
        fun refresh()

        /**
         * Redirect the user to news details screen.
         *
         * @param bundle    Provide a bundle which will be returned via
         *                  [View.openNewsDetailsActivity] later on with extra details which you
         *                  can transport to News details screen.
         * @param article   Extra news information
         */
        fun redirectToNewsDetailsScreen(bundle: Bundle, article: ArticlesItem?)

        /**
         * Should be called when last 5th news item is visible on the screen.
         */
        fun reachedLastFifthNewsItem()

        fun recyclerLoadMoreErrorViewClicked()
    }
}