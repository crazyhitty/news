package com.crazyhitty.chdev.ks.news.newsListing

import android.os.Bundle
import com.crazyhitty.chdev.ks.news.base.Presenter
import com.crazyhitty.chdev.ks.news.data.Constants
import com.crazyhitty.chdev.ks.news.data.api.NewsApiService
import com.crazyhitty.chdev.ks.news.data.api.model.news.ArticlesItem
import com.crazyhitty.chdev.ks.news.data.api.model.news.News
import com.crazyhitty.chdev.ks.news.util.DateTimeFormatter
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
                                               private val newsApiService: NewsApiService,
                                               private val dateTimeFormatter: DateTimeFormatter) :
        Presenter<NewsListingContract.View>(), NewsListingContract.Presenter {
    private val log = AnkoLogger(this::class.java)

    private var page = 1

    override fun onAttach(view: NewsListingContract.View) {
        super.onAttach(view)
        // Check if internet is available or not.
        if (internetHelper.isAvailable()) {
            this.view.showProgress()
            this.view.disableRefresh()
            compositeDisposable.add(newsApiService.everything("ars-technica", 20, page)
                    .map { cleanNewsData(it) }
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe({
                        // Handle success scenario.
                        log.info { "News loaded from remote server" }

                        // Check if news status is ok and if news articles are available.
                        if (it?.status.equals("ok") &&
                                it?.articles?.isNotEmpty() == true) {
                            this.view.hideProgress()
                            this.view.showNewsArticles(it.articles as ArrayList<ArticlesItem?>)
                            this.view.enableRefresh()
                        } else {
                            log.error {
                                """
                                    Unable to display news
                                    Cause:
                                    status=${it?.status}
                                    articlesSize=${it?.articles?.size}
                                """.trimIndent()
                            }
                            this.view.hideProgress()
                            this.view.showError("Unknown error")
                            this.view.enableRefresh()
                        }
                    }, {
                        // Handle failure scenario.
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
                    }))
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
            compositeDisposable.add(newsApiService.everything("ars-technica", 20, page)
                    .map { cleanNewsData(it) }
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe({
                        // Handle success scenario.
                        log.info { "News loaded from remote server" }

                        // Check if news status is ok and if news articles are available.
                        if (it?.status.equals("ok") &&
                                it?.articles?.isNotEmpty() == true) {
                            this.view.showRefreshingDoneMessage("News refreshed")
                            this.view.stopRefreshing()
                            this.view.clearNews()
                            this.view.showNewsArticles(it.articles as ArrayList<ArticlesItem?>)
                        } else {
                            log.error {
                                """
                                    Unable to display news
                                    Cause:
                                    status=${it?.status}
                                    articlesSize=${it?.articles?.size}
                                """.trimIndent()
                            }
                            this.view.showErrorToast("Unknown error")
                            this.view.stopRefreshing()
                        }
                    }, {
                        // Handle failure scenario.
                        log.error {
                            """
                                Unable to load news from remote server
                                Cause:
                                $it
                            """.trimIndent()
                        }
                        this.view.showErrorToast(it.message ?: "Unknown error")
                        this.view.stopRefreshing()
                    }))
        } else {
            this.view.showErrorToast("No internet available")
            this.view.stopRefreshing()
        }
    }

    override fun redirectToNewsDetailsScreen(bundle: Bundle, article: ArticlesItem?) {
        bundle.putParcelable(Constants.NewsListing.EXTRA_ARTICLES_ITEM, article)
        view.openNewsDetailsActivity(bundle)
    }

    override fun reachedLastFifthNewsItem() {
        // Check if internet is available or not.
        if (internetHelper.isAvailable()) {
            this.view.disableRefresh()
            compositeDisposable.add(newsApiService.everything("ars-technica", 20, page.plus(1))
                    .map { cleanNewsData(it) }
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe({
                        // Handle success scenario.
                        log.info { "News loaded from remote server" }

                        if (it?.status.equals("ok") &&
                                it?.articles?.isNotEmpty() == true) {
                            page = page.plus(1)
                            this.view.showNewsArticles(it.articles as ArrayList<ArticlesItem?>)
                            this.view.enableRefresh()
                        } else {
                            log.error {
                                """
                                    Unable to display news
                                    Cause:
                                    status=${it?.status}
                                    articlesSize=${it?.articles?.size}
                                """.trimIndent()
                            }
                            this.view.showRecyclerLoadMoreErrorView("Unknown error")
                            this.view.showErrorToast("Unknown error")
                            this.view.enableRefresh()
                        }
                    }, {
                        // Handle failure scenario.
                        log.error {
                            """
                                Unable to load news from remote server
                                Cause:
                                $it
                            """.trimIndent()
                        }
                        this.view.showRecyclerLoadMoreErrorView(it.message ?: "Unknown error")
                        this.view.showErrorToast(it.message ?: "Unknown error")
                        this.view.enableRefresh()
                    }))
        } else {
            this.view.showRecyclerLoadMoreErrorView("No internet available")
        }
    }

    /**
     * Cleans the news data and removes unwanted articles from the list.
     *
     * @param news  Existing news data
     *
     * @return
     * Filtered and cleaned news.
     */
    private fun cleanNewsData(news: News): News {
        val filteredArticles = news.articles?.filter { !it?.title.isNullOrBlank() }
                ?.distinctBy { Pair(it?.title, it?.description) }
                ?.map {
                    // Convert publish date into a readable date.
                    if (it?.publishedAt != null) {
                        it.publishedAtReadable = dateTimeFormatter.convertPublishDateToReadable(it.publishedAt)
                    }
                    // Return the current article with readable date.
                    it
                }
        return news.copy(totalResults = news.totalResults,
                articles = filteredArticles,
                status = news.status)
    }
}