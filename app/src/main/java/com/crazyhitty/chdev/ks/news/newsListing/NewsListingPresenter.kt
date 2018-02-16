package com.crazyhitty.chdev.ks.news.newsListing

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
 * Implementation of [NewsListingContract.Presenter].
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class NewsListingPresenter @Inject constructor(private val internetHelper: InternetHelper,
                                               private val schedulerProvider: SchedulerProvider,
                                               private val compositeDisposable: CompositeDisposable,
                                               private val newsApiService: NewsApiService) : NewsListingContract.Presenter {
    private val log = AnkoLogger(this::class.java)

    private var view: NewsListingContract.View? = null

    override fun onViewCreated(view: NewsListingContract.View) {
        this.view = view

        // Check if internet is available or not.
        if (internetHelper.isAvailable()) {
            compositeDisposable.add(newsApiService.topHeadlines("us", 0)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .doOnSuccess {
                        log.info { "News loaded from remote server" }
                        this.view?.showNews(it)
                    }
                    .doOnError {
                        log.error {
                            """
                                Unable to load news from remote server
                                Cause:
                                $it
                            """.trimIndent()
                        }
                        this.view?.showError(it.message ?: "Unknown error")
                    }
                    .subscribe())
        } else {
            this.view?.showError("No internet available")
        }
    }

    override fun onViewDestroyed() {
        view = null
        compositeDisposable.clear()
    }

    override fun refresh() {

    }

    override fun redirectToNewsDetailsScreen(articlesItem: ArticlesItem) {

    }
}