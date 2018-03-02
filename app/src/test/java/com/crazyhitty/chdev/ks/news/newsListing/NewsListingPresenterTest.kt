package com.crazyhitty.chdev.ks.news.newsListing

import android.os.Bundle
import com.crazyhitty.chdev.ks.news.data.Constants
import com.crazyhitty.chdev.ks.news.data.api.NewsApiService
import com.crazyhitty.chdev.ks.news.data.api.model.news.ArticlesItem
import com.crazyhitty.chdev.ks.news.data.api.model.news.News
import com.crazyhitty.chdev.ks.news.data.api.model.news.Source
import com.crazyhitty.chdev.ks.news.util.DateTimeFormatter
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
import java.text.SimpleDateFormat
import java.util.*

/**
 * Tests for [NewsListingPresenter].
 *
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
    private lateinit var dateTimeFormatter: DateTimeFormatter
    private lateinit var newsListingPresenter: NewsListingContract.Presenter

    @Before
    fun setup() {
        val compositeDisposable = CompositeDisposable()

        testScheduler = TestScheduler()
        val testSchedulerProvider = TestSchedulerProvider(testScheduler)

        dateTimeFormatter = DateTimeFormatter(SimpleDateFormat(Constants.DateFormat.PROVIDED_DATE_FORMAT,
                Locale.getDefault()))

        newsListingPresenter = NewsListingPresenter(mockInternetHelper,
                testSchedulerProvider,
                compositeDisposable,
                mockNewsApiService,
                dateTimeFormatter)
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
        val articles = arrayListOf(ArticlesItem("2018-02-21T20:12:43Z",
                null,
                "John Doe",
                "https://example.com/example.png",
                "John Doe is not a dummy name",
                Source("1", "Sellout News Network"),
                "John Does goes to Hollywood",
                "https://example.com/example"))
        val news = News(20, articles, "ok")

        Mockito.doReturn(Single.just(news))
                .`when`(mockNewsApiService)
                .everything("ars-technica", 20, 1)

        newsListingPresenter.onAttach(mockNewsListingView)

        testScheduler.triggerActions()

        Mockito.verify(mockNewsListingView).showProgress()
        Mockito.verify(mockNewsListingView).disableRefresh()
        Mockito.verify(mockNewsListingView).hideProgress()
        Mockito.verify(mockNewsListingView).showNewsArticles(news.articles as ArrayList<ArticlesItem?>)
        Mockito.verify(mockNewsListingView).enableRefresh()
        Mockito.verify(mockNewsListingView).startListeningForLastFifthNewsItemShown()
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
                .everything("ars-technica", 20, 1)

        newsListingPresenter.onAttach(mockNewsListingView)

        testScheduler.triggerActions()

        Mockito.verify(mockNewsListingView).showProgress()
        Mockito.verify(mockNewsListingView).disableRefresh()
        Mockito.verify(mockNewsListingView).hideProgress()
        Mockito.verify(mockNewsListingView).showError("")
        Mockito.verify(mockNewsListingView).enableRefresh()
    }

    /**
     * Test if the [NewsListingPresenter.onAttach] method is executed successfully and works as
     * expected in a scenario where remote server is able to fetch the news but the news returned
     * from server is malformed.
     */
    @Test
    fun testNewsListingPresenterOnAttachApiFailureAlternative() {
        // Mock internet helper and return true for internet connectivity checks.
        Mockito.doReturn(true)
                .`when`(mockInternetHelper)
                .isAvailable()

        val news = News(0, null, "failure")

        Mockito.doReturn(Single.just(news))
                .`when`(mockNewsApiService)
                .everything("ars-technica", 20, 1)

        newsListingPresenter.onAttach(mockNewsListingView)

        testScheduler.triggerActions()

        Mockito.verify(mockNewsListingView).showProgress()
        Mockito.verify(mockNewsListingView).disableRefresh()
        Mockito.verify(mockNewsListingView).hideProgress()
        Mockito.verify(mockNewsListingView).showError("Unknown error")
        Mockito.verify(mockNewsListingView).enableRefresh()
    }

    /**
     * Test if the [NewsListingPresenter.refresh] method is executed successfully and works as
     * expected in a scenario where everything goes fine.
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
        val articles = arrayListOf(ArticlesItem("2018-02-21T20:12:43Z",
                null,
                "John Doe",
                "https://example.com/example.png",
                "John Doe is not a dummy name",
                Source("1", "Sellout News Network"),
                "John Does goes to Hollywood",
                "https://example.com/example"))
        val news = News(20, articles, "ok")

        Mockito.doReturn(Single.just(news))
                .`when`(mockNewsApiService)
                .everything("ars-technica", 20, 1)

        newsListingPresenter.refresh()

        testScheduler.triggerActions()

        Mockito.verify(mockNewsListingView).hideError()
        Mockito.verify(mockNewsListingView).stopListeningForLastFifthNewsItemShown()
        Mockito.verify(mockNewsListingView).showRefreshingDoneMessage("News refreshed")
        Mockito.verify(mockNewsListingView).stopRefreshing()
        Mockito.verify(mockNewsListingView).clearNews()
        Mockito.verify(mockNewsListingView).showNewsArticles(news.articles as ArrayList<ArticlesItem?>)
        Mockito.verify(mockNewsListingView).startListeningForLastFifthNewsItemShown()
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
                .everything("ars-technica", 20, 1)

        newsListingPresenter.refresh()

        testScheduler.triggerActions()

        Mockito.verify(mockNewsListingView).stopListeningForLastFifthNewsItemShown()
        Mockito.verify(mockNewsListingView).showErrorToast("")
        Mockito.verify(mockNewsListingView).stopRefreshing()
        Mockito.verify(mockNewsListingView).startListeningForLastFifthNewsItemShown()
    }

    /**
     * Test if the [NewsListingPresenter.refresh] method is executed successfully and works as
     * expected in a scenario where remote server is able to fetch the news but the news returned
     * from server is malformed.
     */
    @Test
    fun testNewsListingPresenterRefreshApiFailureAlternative() {
        // Just attach the view to the presenter.
        newsListingPresenter.onAttach(mockNewsListingView)

        // Mock internet helper and return true for internet connectivity checks.
        Mockito.doReturn(true)
                .`when`(mockInternetHelper)
                .isAvailable()

        val news = News(0, null, "failure")

        Mockito.doReturn(Single.just(news))
                .`when`(mockNewsApiService)
                .everything("ars-technica", 20, 1)

        newsListingPresenter.refresh()

        testScheduler.triggerActions()

        Mockito.verify(mockNewsListingView).stopListeningForLastFifthNewsItemShown()
        Mockito.verify(mockNewsListingView).showErrorToast("Unknown error")
        Mockito.verify(mockNewsListingView).stopRefreshing()
        Mockito.verify(mockNewsListingView).startListeningForLastFifthNewsItemShown()
    }

    /**
     * Test if the [NewsListingPresenter.newsItemClicked] method is executed
     * successfully and works as expected in a scenario where everything goes fine.
     */
    @Test
    fun testNewsListingPresenterRedirectToNewsDetailsScreenSuccess() {
        // Just attach the view to the presenter.
        newsListingPresenter.onAttach(mockNewsListingView)

        val articlesItem = ArticlesItem()

        newsListingPresenter.newsItemClicked(mockBundle, articlesItem)

        Mockito.verify(mockNewsListingView).openNewsDetailsActivity(mockBundle)
    }

    /**
     * Test if the [NewsListingPresenter.reachedLastFifthNewsItem] method is executed successfully
     * and works as expected in a scenario where everything goes fine.
     */
    @Test
    fun testReachedLastFifthNewsItemSuccess() {
        // Just attach the view to the presenter.
        newsListingPresenter.onAttach(mockNewsListingView)

        // Mock internet helper and return true for internet connectivity checks.
        Mockito.doReturn(true)
                .`when`(mockInternetHelper)
                .isAvailable()

        // Mock NewsApiService and return a dummy news data when topHeadlines method is called.
        val articles = arrayListOf(ArticlesItem("2018-02-21T20:12:43Z",
                null,
                "John Doe",
                "https://example.com/example.png",
                "John Doe is not a dummy name",
                Source("1", "Sellout News Network"),
                "John Does goes to Hollywood",
                "https://example.com/example"))
        val news = News(20, articles, "ok")

        Mockito.doReturn(Single.just(news))
                .`when`(mockNewsApiService)
                .everything("ars-technica", 20, 2)

        newsListingPresenter.reachedLastFifthNewsItem()

        testScheduler.triggerActions()

        Mockito.verify(mockNewsListingView).disableRefresh()
        Mockito.verify(mockNewsListingView).showNewsArticles(news.articles as ArrayList<ArticlesItem?>)
        Mockito.verify(mockNewsListingView).enableRefresh()
    }

    /**
     * Test if the [NewsListingPresenter.reachedLastFifthNewsItem] method is executed successfully
     * and works as expected in a scenario where internet is not available.
     */
    @Test
    fun testReachedLastFifthNewsItemInternetFailure() {
        // Just attach the view to the presenter.
        newsListingPresenter.onAttach(mockNewsListingView)

        // Mock internet helper and return true for internet connectivity checks.
        Mockito.doReturn(false)
                .`when`(mockInternetHelper)
                .isAvailable()

        newsListingPresenter.reachedLastFifthNewsItem()

        testScheduler.triggerActions()

        Mockito.verify(mockNewsListingView).showRecyclerLoadMoreErrorView("No internet available")
    }

    /**
     * Test if the [NewsListingPresenter.reachedLastFifthNewsItem] method is executed successfully
     * and works as expected in a scenario where remote server is unable to fetch the news.
     */
    @Test
    fun testReachedLastFifthNewsItemApiFailure() {
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
                .everything("ars-technica", 20, 2)

        newsListingPresenter.reachedLastFifthNewsItem()

        testScheduler.triggerActions()

        Mockito.verify(mockNewsListingView).showRecyclerLoadMoreErrorView("")
        Mockito.verify(mockNewsListingView).showErrorToast("")
        Mockito.verify(mockNewsListingView).enableRefresh()
    }

    /**
     * Test if the [NewsListingPresenter.reachedLastFifthNewsItem] method is executed successfully
     * and works as expected in a scenario where remote server is able to fetch the news but the
     * news returned from server is malformed.
     */
    @Test
    fun testReachedLastFifthNewsItemApiFailureAlternative() {
        // Just attach the view to the presenter.
        newsListingPresenter.onAttach(mockNewsListingView)

        // Mock internet helper and return true for internet connectivity checks.
        Mockito.doReturn(true)
                .`when`(mockInternetHelper)
                .isAvailable()

        val news = News(0, null, "failure")

        Mockito.doReturn(Single.just(news))
                .`when`(mockNewsApiService)
                .everything("ars-technica", 20, 2)

        newsListingPresenter.reachedLastFifthNewsItem()

        testScheduler.triggerActions()

        Mockito.verify(mockNewsListingView).showRecyclerLoadMoreErrorView("Unknown error")
        Mockito.verify(mockNewsListingView).showErrorToast("Unknown error")
        Mockito.verify(mockNewsListingView).enableRefresh()
    }

    /**
     * Test if the [NewsListingPresenter.recyclerLoadMoreErrorViewClicked] method is executed
     * successfully and works as expected in a scenario where everything goes fine.
     */
    @Test
    fun testRecyclerLoadMoreErrorViewClickedSuccess() {
        // Just attach the view to the presenter.
        newsListingPresenter.onAttach(mockNewsListingView)

        // Mock internet helper and return true for internet connectivity checks.
        Mockito.doReturn(true)
                .`when`(mockInternetHelper)
                .isAvailable()

        // Mock NewsApiService and return a dummy news data when topHeadlines method is called.
        val articles = arrayListOf(ArticlesItem("2018-02-21T20:12:43Z",
                null,
                "John Doe",
                "https://example.com/example.png",
                "John Doe is not a dummy name",
                Source("1", "Sellout News Network"),
                "John Does goes to Hollywood",
                "https://example.com/example"))
        val news = News(20, articles, "ok")

        Mockito.doReturn(Single.just(news))
                .`when`(mockNewsApiService)
                .everything("ars-technica", 20, 2)

        newsListingPresenter.recyclerLoadMoreErrorViewClicked()

        testScheduler.triggerActions()

        Mockito.verify(mockNewsListingView).disableRefresh()
        Mockito.verify(mockNewsListingView).showRecyclerLoadingView()
        Mockito.verify(mockNewsListingView).showNewsArticles(news.articles as ArrayList<ArticlesItem?>)
        Mockito.verify(mockNewsListingView).enableRefresh()
    }

    /**
     * Test if the [NewsListingPresenter.recyclerLoadMoreErrorViewClicked] method is executed
     * successfully and works as expected in a scenario where internet is not available.
     */
    @Test
    fun testRecyclerLoadMoreErrorViewClickedInternetFailure() {
        // Just attach the view to the presenter.
        newsListingPresenter.onAttach(mockNewsListingView)

        // Mock internet helper and return true for internet connectivity checks.
        Mockito.doReturn(false)
                .`when`(mockInternetHelper)
                .isAvailable()

        newsListingPresenter.recyclerLoadMoreErrorViewClicked()

        testScheduler.triggerActions()

        Mockito.verify(mockNewsListingView).showErrorToast("No internet available")
        Mockito.verify(mockNewsListingView).showRecyclerLoadMoreErrorView("No internet available")
    }

    /**
     * Test if the [NewsListingPresenter.recyclerLoadMoreErrorViewClicked] method is executed
     * successfully and works as expected in a scenario where remote server is unable to fetch the
     * news.
     */
    @Test
    fun testRecyclerLoadMoreErrorViewClickedApiFailure() {
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
                .everything("ars-technica", 20, 2)

        newsListingPresenter.recyclerLoadMoreErrorViewClicked()

        testScheduler.triggerActions()

        Mockito.verify(mockNewsListingView).showRecyclerLoadMoreErrorView("")
        Mockito.verify(mockNewsListingView).showErrorToast("")
        Mockito.verify(mockNewsListingView).enableRefresh()
    }

    /**
     * Test if the [NewsListingPresenter.recyclerLoadMoreErrorViewClicked] method is executed
     * successfully and works as expected in a scenario where remote server is able to fetch the
     * news but the news returned from server is malformed.
     */
    @Test
    fun testRecyclerLoadMoreErrorViewClickedApiFailureAlternative() {
        // Just attach the view to the presenter.
        newsListingPresenter.onAttach(mockNewsListingView)

        // Mock internet helper and return true for internet connectivity checks.
        Mockito.doReturn(true)
                .`when`(mockInternetHelper)
                .isAvailable()

        val news = News(0, null, "failure")

        Mockito.doReturn(Single.just(news))
                .`when`(mockNewsApiService)
                .everything("ars-technica", 20, 2)

        newsListingPresenter.recyclerLoadMoreErrorViewClicked()

        testScheduler.triggerActions()

        Mockito.verify(mockNewsListingView).showRecyclerLoadMoreErrorView("Unknown error")
        Mockito.verify(mockNewsListingView).showErrorToast("Unknown error")
        Mockito.verify(mockNewsListingView).enableRefresh()
    }

    @After
    fun tearDown() {
        newsListingPresenter.onDetach()
    }
}