package com.crazyhitty.chdev.ks.news.newsListing

import android.os.Bundle
import com.crazyhitty.chdev.ks.news.data.api.NewsApiService
import com.crazyhitty.chdev.ks.news.data.api.model.news.ArticlesItem
import com.crazyhitty.chdev.ks.news.data.api.model.news.News
import com.crazyhitty.chdev.ks.news.util.internet.InternetHelper
import com.crazyhitty.chdev.ks.news.util.rx.TestSchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
@RunWith(MockitoJUnitRunner::class)
class NewsListingPresenterTest {
    @Mock
    private lateinit var mockInternetHelper: InternetHelper
    @Mock
    private lateinit var mockNewsApiService: NewsApiService
    @Mock
    private lateinit var mockNewsListingView: NewsListingContract.View
    @Mock
    private lateinit var mockBundle: Bundle

    private lateinit var testScheduler: TestScheduler
    private lateinit var newsListingPresenter: NewsListingContract.Presenter

    @Before
    fun setup() {
        val compositeDisposable = CompositeDisposable()

        testScheduler = TestScheduler()
        val testSchedulerProvider = TestSchedulerProvider(testScheduler)

        newsListingPresenter = NewsListingPresenter(mockInternetHelper,
                testSchedulerProvider,
                compositeDisposable,
                mockNewsApiService)
    }

    /**
     * Test if the [NewsListingPresenter.onAttach] method is executed successfully and works as
     * expected in a scenario where everything goes fine.
     */
    @Test
    fun testNewsListingPresenterOnAttachSuccess() {
        // Mock internet helper and return true for internet connectivity checks.
        Mockito.doReturn(true)
                .`when`(mockInternetHelper)
                .isAvailable()

        // Mock NewsApiService and return a dummy news data when topHeadlines method is called.
        val news = News()

        Mockito.doReturn(Single.just(news))
                .`when`(mockNewsApiService)
                .topHeadlines("us", 0)

        newsListingPresenter.onAttach(mockNewsListingView)

        testScheduler.triggerActions()

        Mockito.verify(mockNewsListingView).showProgress()
        Mockito.verify(mockNewsListingView).disableRefresh()
        Mockito.verify(mockNewsListingView).hideProgress()
        Mockito.verify(mockNewsListingView).showNews(news)
        Mockito.verify(mockNewsListingView).enableRefresh()
    }

    /**
     * Test if the [NewsListingPresenter.onAttach] method is executed successfully and works as
     * expected in a scenario where internet is not available.
     */
    @Test
    fun testNewsListingPresenterOnAttachInternetFailure() {
        // Mock internet helper and return true for internet connectivity checks.
        Mockito.doReturn(false)
                .`when`(mockInternetHelper)
                .isAvailable()

        newsListingPresenter.onAttach(mockNewsListingView)

        Mockito.verify(mockNewsListingView).showError("No internet available")
    }

    /**
     * Test if the [NewsListingPresenter.onAttach] method is executed successfully and works as
     * expected in a scenario where remote server is unable to fetch the news.
     */
    @Test
    fun testNewsListingPresenterOnAttachApiFailure() {
        // Mock internet helper and return true for internet connectivity checks.
        Mockito.doReturn(true)
                .`when`(mockInternetHelper)
                .isAvailable()

        val singleNewsError = Single.create<News> {
            it.onError(Exception(""))
        }

        Mockito.doReturn(singleNewsError)
                .`when`(mockNewsApiService)
                .topHeadlines("us", 0)

        newsListingPresenter.onAttach(mockNewsListingView)

        testScheduler.triggerActions()

        Mockito.verify(mockNewsListingView).showProgress()
        Mockito.verify(mockNewsListingView).disableRefresh()
        Mockito.verify(mockNewsListingView).hideProgress()
        Mockito.verify(mockNewsListingView).showError("")
        Mockito.verify(mockNewsListingView).enableRefresh()
    }

    /**
     * Test if the [NewsListingPresenter.refresh] method is executed successfully and works as
     * expected in a scenario where internet is not available.
     */
    @Test
    fun testNewsListingPresenterRefreshSuccess() {
        // Just attach the view to the presenter.
        newsListingPresenter.onAttach(mockNewsListingView)

        // Mock internet helper and return true for internet connectivity checks.
        Mockito.doReturn(true)
                .`when`(mockInternetHelper)
                .isAvailable()

        // Mock NewsApiService and return a dummy news data when topHeadlines method is called.
        val news = News()

        Mockito.doReturn(Single.just(news))
                .`when`(mockNewsApiService)
                .topHeadlines("us", 0)

        newsListingPresenter.refresh()

        testScheduler.triggerActions()

        Mockito.verify(mockNewsListingView).showRefreshingDoneMessage("News refreshed")
        Mockito.verify(mockNewsListingView).stopRefreshing()
        Mockito.verify(mockNewsListingView).clearNews()
        Mockito.verify(mockNewsListingView).showNews(news)
    }

    /**
     * Test if the [NewsListingPresenter.refresh] method is executed successfully and works as
     * expected in a scenario where internet is not available.
     */
    @Test
    fun testNewsListingPresenterRefreshInternetFailure() {
        // Just attach the view to the presenter.
        newsListingPresenter.onAttach(mockNewsListingView)

        // Mock internet helper and return true for internet connectivity checks.
        Mockito.doReturn(false)
                .`when`(mockInternetHelper)
                .isAvailable()

        newsListingPresenter.refresh()

        Mockito.verify(mockNewsListingView).showErrorToast("No internet available")
        Mockito.verify(mockNewsListingView).stopRefreshing()
    }

    /**
     * Test if the [NewsListingPresenter.refresh] method is executed successfully and works as
     * expected in a scenario where remote server is unable to fetch the news.
     */
    @Test
    fun testNewsListingPresenterRefreshApiFailure() {
        // Just attach the view to the presenter.
        newsListingPresenter.onAttach(mockNewsListingView)

        // Mock internet helper and return true for internet connectivity checks.
        Mockito.doReturn(true)
                .`when`(mockInternetHelper)
                .isAvailable()

        val singleNewsError = Single.create<News> {
            it.onError(Exception(""))
        }

        Mockito.doReturn(singleNewsError)
                .`when`(mockNewsApiService)
                .topHeadlines("us", 0)

        newsListingPresenter.refresh()

        testScheduler.triggerActions()

        Mockito.verify(mockNewsListingView).showErrorToast("")
        Mockito.verify(mockNewsListingView).stopRefreshing()
    }

    /**
     * Test if the [NewsListingPresenter.redirectToNewsDetailsScreen] method is executed
     * successfully and works as expected in a scenario where everything goes fine.
     */
    @Test
    fun testNewsListingPresenterRedirectToNewsDetailsScreenSuccess() {
        // Just attach the view to the presenter.
        newsListingPresenter.onAttach(mockNewsListingView)

        val articlesItem = ArticlesItem()

        newsListingPresenter.redirectToNewsDetailsScreen(mockBundle, articlesItem)

        Mockito.verify(mockNewsListingView).openNewsDetailsActivity(mockBundle)
    }

    @After
    fun tearDown() {
        newsListingPresenter.onDetach()
    }
}