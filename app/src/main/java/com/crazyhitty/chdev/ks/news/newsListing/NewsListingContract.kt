package com.crazyhitty.chdev.ks.news.newsListing

import android.os.Bundle
import com.crazyhitty.chdev.ks.news.data.model.news.ArticlesItem
import com.crazyhitty.chdev.ks.news.data.model.news.News

/**
 * Contains blueprint for View and Presenter responsible for showing and getting news.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
interface NewsListingContract {

    interface View {
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
         * Show error message if something goes wrong.
         */
        fun showError(message: String)
    }

    interface Presenter {
        /**
         * Should be called when view is created.
         */
        fun onViewCreated(view: NewsListingContract.View)

        /**
         * Should be called when view is destroyed.
         */
        fun onViewDestroyed()

        /**
         * Refreshes the news.
         */
        fun refresh()

        /**
         * Redirect the user to news details screen.
         *
         * @param articlesItem  Extra news information
         */
        fun redirectToNewsDetailsScreen(articlesItem: ArticlesItem)
    }
}