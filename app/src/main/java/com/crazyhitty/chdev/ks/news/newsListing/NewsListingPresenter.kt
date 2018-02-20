package com.crazyhitty.chdev.ks.news.newsListing

import android.os.Bundle
import com.crazyhitty.chdev.ks.news.base.Presenter
import com.crazyhitty.chdev.ks.news.data.Constants
import com.crazyhitty.chdev.ks.news.data.NewsApiService
import com.crazyhitty.chdev.ks.news.data.model.news.ArticlesItem
import com.crazyhitty.chdev.ks.news.util.internet.InternetHelper
import com.crazyhitty.chdev.ks.news.util.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import javax.inject.Inject

/**
 * Implementation of [NewsListingContract.Presenter]. The main job of this presenter is to fetch
 * news from remote server and present it to the UI.
 *
 * @param internetHelper        [InternetHelper]'s instance to monitor internet availability.
 * @param schedulerProvider     [SchedulerProvider]'s instance to run code on appropriate threads.
 * @param compositeDisposable   [CompositeDisposable]'s instance for managing multiple disposables.
 * @param newsApiService        [NewsApiService]'s instance for fetching news data from remote
 *                              server.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class NewsListingPresenter @Inject constructor(private val internetHelper: InternetHelper,
                                               private val schedulerProvider: SchedulerProvider,
                                               private val compositeDisposable: CompositeDisposable,
                                               private val newsApiService: NewsApiService) :
        Presenter<NewsListingContract.View>(), NewsListingContract.Presenter {
    private val log = AnkoLogger(this::class.java)

    override fun onAttach(view: NewsListingContract.View) {
        super.onAttach(view)
        // Check if internet is available or not.
        if (internetHelper.isAvailable()) {
            this.view.showProgress()
            this.view.disableRefresh()
            compositeDisposable.add(newsApiService.topHeadlines("us", 0)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .doOnSuccess {
                        log.info { "News loaded from remote server" }
                        this.view.hideProgress()
                        this.view.showNews(it)
                        this.view.enableRefresh()
                    }
                    .doOnError {
                        log.error {
                            """
                                Unable to load news from remote server
                                Cause:
                                $it
                            """.trimIndent()
                        }
                        this.view.hideProgress()
                        this.view.showError(it.message ?: "Unknown error")
                        this.view.enableRefresh()
                    }
                    .subscribe())
        } else {
            this.view.showError("No internet available")
        }
    }

    override fun onDetach() {
        compositeDisposable.clear()
        super.onDetach()
    }

    override fun refresh() {
        // Check if internet is available or not.
        if (internetHelper.isAvailable()) {
            compositeDisposable.add(newsApiService.topHeadlines("us", 0)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .doOnSuccess {
                        log.info { "News loaded from remote server" }
                        this.view.showRefreshingDoneMessage("News refreshed")
                        this.view.stopRefreshing()
                        this.view.clearNews()
                        this.view.showNews(it)
                    }
                    .doOnError {
                        log.error {
                            """
                                Unable to load news from remote server
                                Cause:
                                $it
                            """.trimIndent()
                        }
                        this.view.showErrorToast(it.message ?: "Unknown error")
                        this.view.stopRefreshing()
                    }
                    .subscribe())
        } else {
            this.view.showErrorToast("No internet available")
            this.view.stopRefreshing()
        }
    }

    override fun redirectToNewsDetailsScreen(articlesItem: ArticlesItem?) {
        val bundle = Bundle()
        bundle.putParcelable(Constants.NewsListing.EXTRA_ARTICLES_ITEM, articlesItem)
        view.openNewsDetailsActivity(bundle)
    }
}