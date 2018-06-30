package com.crazyhitty.chdev.ks.news.sources

import com.crazyhitty.chdev.ks.news.data.DataStore
import com.crazyhitty.chdev.ks.news.data.api.NewsApiService
import com.crazyhitty.chdev.ks.news.data.api.model.news.SourceItem
import com.crazyhitty.chdev.ks.news.data.api.model.news.Sources
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
 * Tests for [SourcesPresenter].
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
@RunWith(MockitoJUnitRunner::class)
class SourcesPresenterTest {
    @Mock
    private lateinit var mockInternetHelper: InternetHelper
    @Mock
    private lateinit var mockNewsApiService: NewsApiService
    @Mock
    private lateinit var mockDataStore: DataStore
    @Mock
    private lateinit var mockSourcesView: SourcesContract.View

    private lateinit var testScheduler: TestScheduler
    private lateinit var sourcesPresenter: SourcesPresenter

    @Before
    fun setUp() {
        val compositeDisposable = CompositeDisposable()

        testScheduler = TestScheduler()
        val testSchedulerProvider = TestSchedulerProvider(testScheduler)

        sourcesPresenter = SourcesPresenter(mockInternetHelper,
                testSchedulerProvider,
                compositeDisposable,
                mockNewsApiService,
                mockDataStore)
    }

    /**
     * Test if the [SourcesPresenter.onAttach] method is executed successfully and works as
     * expected in a scenario where everything goes fine.
     */
    @Test
    fun testSourcesPresenterOnAttachSuccess() {
        // Mock internet helper and return true for internet connectivity checks.
        Mockito.doReturn(true)
                .`when`(mockInternetHelper)
                .isAvailable()

        // Mock NewsApiService and return a dummy sources data when sources method is called.
        val sourceItems = arrayListOf(SourceItem("ars-technica",
                "Ars Technica",
                "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                "http://arstechnica.com",
                "technology",
                "en",
                "us"))
        val sources = Sources("ok", sourceItems)

        Mockito.doReturn(Single.just(sources))
                .`when`(mockNewsApiService)
                .sources()

        sourcesPresenter.onAttach(mockSourcesView)

        testScheduler.triggerActions()

        Mockito.verify(mockSourcesView).showProgress()
        Mockito.verify(mockSourcesView).disableSearch()
        Mockito.verify(mockSourcesView).disableRefresh()
        Mockito.verify(mockSourcesView).disableContinueFooter()
        Mockito.verify(mockSourcesView).hideContinueFooter()
        Mockito.verify(mockSourcesView).hideProgress()
        Mockito.verify(mockSourcesView).showSources(sources.sources as ArrayList<SourceItem?>)
        Mockito.verify(mockSourcesView).enableSearch()
        Mockito.verify(mockSourcesView).enableRefresh()
        Mockito.verify(mockSourcesView).showContinueFooter()
    }

    /**
     * Test if the [SourcesPresenter.onAttach] method is executed successfully and works as
     * expected in a scenario where internet is not available.
     */
    @Test
    fun testSourcesPresenterOnAttachInternetFailure() {
        // Mock internet helper and return false for internet connectivity checks.
        Mockito.doReturn(false)
                .`when`(mockInternetHelper)
                .isAvailable()

        sourcesPresenter.onAttach(mockSourcesView)

        Mockito.verify(mockSourcesView).showError("No internet available")
    }

    /**
     * Test if the [SourcesPresenter.onAttach] method is executed successfully and works as
     * expected in a scenario where remote server is unable to fetch the sources.
     */
    @Test
    fun testSourcesPresenterOnAttachApiFailure() {
        // Mock internet helper and return false for internet connectivity checks.
        Mockito.doReturn(true)
                .`when`(mockInternetHelper)
                .isAvailable()

        val singleSourcesError = Single.create<Sources> {
            it.onError(Exception(""))
        }

        Mockito.doReturn(singleSourcesError)
                .`when`(mockNewsApiService)
                .sources()

        sourcesPresenter.onAttach(mockSourcesView)

        testScheduler.triggerActions()

        Mockito.verify(mockSourcesView).showProgress()
        Mockito.verify(mockSourcesView).disableSearch()
        Mockito.verify(mockSourcesView).disableRefresh()
        Mockito.verify(mockSourcesView).disableContinueFooter()
        Mockito.verify(mockSourcesView).hideContinueFooter()
        Mockito.verify(mockSourcesView).hideProgress()
        Mockito.verify(mockSourcesView).showError("")
        Mockito.verify(mockSourcesView).enableRefresh()
    }

    /**
     * Test if the [SourcesPresenter.onAttach] method is executed successfully and works as
     * expected in a scenario where remote server is able to fetch the sources but the sources
     * returned from server is malformed.
     */
    @Test
    fun testSourcesPresenterOnAttachApiFailureAlternative() {
        // Mock internet helper and return false for internet connectivity checks.
        Mockito.doReturn(true)
                .`when`(mockInternetHelper)
                .isAvailable()

        val sources = Sources("failure", null)

        Mockito.doReturn(Single.just(sources))
                .`when`(mockNewsApiService)
                .sources()

        sourcesPresenter.onAttach(mockSourcesView)

        testScheduler.triggerActions()

        Mockito.verify(mockSourcesView).showProgress()
        Mockito.verify(mockSourcesView).disableSearch()
        Mockito.verify(mockSourcesView).disableRefresh()
        Mockito.verify(mockSourcesView).disableContinueFooter()
        Mockito.verify(mockSourcesView).hideContinueFooter()
        Mockito.verify(mockSourcesView).hideProgress()
        Mockito.verify(mockSourcesView).showError("Unknown error")
        Mockito.verify(mockSourcesView).enableRefresh()
    }

    /**
     * Test if the [SourcesPresenter.searchFilterChanged] method is executed successfully and
     * works as expected in a scenario where everything goes fine. In this scenario, 3 items should
     * be filtered and should also be selected.
     */
    @Test
    fun testSourcesPresenterSearchFilterChangedSuccess() {
        // Just attach the view to the presenter.
        sourcesPresenter.onAttach(mockSourcesView)

        val filter = "Ars"

        val sourceItems = arrayListOf(
                SourceItem("ars-technica-1",
                        "Ars Technica 1",
                        "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                        "http://arstechnica.com",
                        "technology",
                        "en",
                        "us"),
                SourceItem("ars-technica-2",
                        "Ars Technica 2",
                        "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                        "http://arstechnica.com",
                        "technology",
                        "en",
                        "us"),
                SourceItem("ars-technica-3",
                        "Ars Technica 3",
                        "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                        "http://arstechnica.com",
                        "technology",
                        "en",
                        "us"))
        sourcesPresenter.cachedSources = Sources("ok", sourceItems)

        sourcesPresenter.selectedCachedSourcesMap["ars-technica-1"] = SourceItem("ars-technica",
                "Ars Technica 1",
                "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                "http://arstechnica.com",
                "technology",
                "en",
                "us")
        sourcesPresenter.selectedCachedSourcesMap["ars-technica-2"] = SourceItem("ars-technica",
                "Ars Technica 2",
                "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                "http://arstechnica.com",
                "technology",
                "en",
                "us")
        sourcesPresenter.selectedCachedSourcesMap["ars-technica-3"] = SourceItem("ars-technica",
                "Ars Technica 3",
                "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                "http://arstechnica.com",
                "technology",
                "en",
                "us")

        sourcesPresenter.searchFilterChanged(filter)

        testScheduler.triggerActions()

        Mockito.verify(mockSourcesView).showClearSearchButton()
        Mockito.verify(mockSourcesView).clearSources()
        Mockito.verify(mockSourcesView).hideError()
        Mockito.verify(mockSourcesView).showSources(sourcesPresenter.cachedSources?.sources as ArrayList<SourceItem?>)
        Mockito.verify(mockSourcesView).disableContinueFooter()
        Mockito.verify(mockSourcesView).showContinueFooter()
        Mockito.verify(mockSourcesView).enableContinueFooter()
    }

    /**
     * Test if the [SourcesPresenter.searchFilterChanged] method is executed successfully and
     * works as expected in a scenario where everything goes fine when the filtered text doesn't
     * matches any available items.
     */
    @Test
    fun testSourcesPresenterSearchFilterChangedFailure() {
        // Just attach the view to the presenter.
        sourcesPresenter.onAttach(mockSourcesView)

        val filter = "BBC"

        val sourceItems = arrayListOf(
                SourceItem("ars-technica-1",
                        "Ars Technica 1",
                        "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                        "http://arstechnica.com",
                        "technology",
                        "en",
                        "us"),
                SourceItem("ars-technica-2",
                        "Ars Technica 2",
                        "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                        "http://arstechnica.com",
                        "technology",
                        "en",
                        "us"),
                SourceItem("ars-technica-3",
                        "Ars Technica 3",
                        "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                        "http://arstechnica.com",
                        "technology",
                        "en",
                        "us"))
        sourcesPresenter.cachedSources = Sources("ok", sourceItems)

        sourcesPresenter.selectedCachedSourcesMap["ars-technica-1"] = SourceItem("ars-technica",
                "Ars Technica 1",
                "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                "http://arstechnica.com",
                "technology",
                "en",
                "us")
        sourcesPresenter.selectedCachedSourcesMap["ars-technica-2"] = SourceItem("ars-technica",
                "Ars Technica 2",
                "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                "http://arstechnica.com",
                "technology",
                "en",
                "us")
        sourcesPresenter.selectedCachedSourcesMap["ars-technica-3"] = SourceItem("ars-technica",
                "Ars Technica 3",
                "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                "http://arstechnica.com",
                "technology",
                "en",
                "us")

        sourcesPresenter.searchFilterChanged(filter)

        testScheduler.triggerActions()

        Mockito.verify(mockSourcesView).showClearSearchButton()
        Mockito.verify(mockSourcesView).clearSources()
        Mockito.verify(mockSourcesView).showError("No such source(s) available")
        Mockito.verify(mockSourcesView).disableContinueFooter()
        Mockito.verify(mockSourcesView).hideContinueFooter()
    }

    /**
     * Test if the [SourcesPresenter.searchFilterChanged] method is executed successfully and
     * works as expected in a scenario where filter text is empty and cached sources are available.
     */
    @Test
    fun testSourcesPresenterSearchFilterChangeFilterEmptyCachedSourcesAvailable() {
        // Just attach the view to the presenter.
        sourcesPresenter.onAttach(mockSourcesView)

        val filter = ""

        val sourceItems = arrayListOf(
                SourceItem("ars-technica-1",
                        "Ars Technica 1",
                        "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                        "http://arstechnica.com",
                        "technology",
                        "en",
                        "us"),
                SourceItem("ars-technica-2",
                        "Ars Technica 2",
                        "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                        "http://arstechnica.com",
                        "technology",
                        "en",
                        "us"),
                SourceItem("ars-technica-3",
                        "Ars Technica 3",
                        "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                        "http://arstechnica.com",
                        "technology",
                        "en",
                        "us"))
        sourcesPresenter.cachedSources = Sources("ok", sourceItems)

        sourcesPresenter.selectedCachedSourcesMap["ars-technica-1"] = SourceItem("ars-technica",
                "Ars Technica 1",
                "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                "http://arstechnica.com",
                "technology",
                "en",
                "us")
        sourcesPresenter.selectedCachedSourcesMap["ars-technica-2"] = SourceItem("ars-technica",
                "Ars Technica 2",
                "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                "http://arstechnica.com",
                "technology",
                "en",
                "us")
        sourcesPresenter.selectedCachedSourcesMap["ars-technica-3"] = SourceItem("ars-technica",
                "Ars Technica 3",
                "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                "http://arstechnica.com",
                "technology",
                "en",
                "us")

        sourcesPresenter.searchFilterChanged(filter)

        testScheduler.triggerActions()

        Mockito.verify(mockSourcesView).hideClearSearchButton()
        Mockito.verify(mockSourcesView).clearSources()
        Mockito.verify(mockSourcesView).hideError()
        Mockito.verify(mockSourcesView).showSources(sourcesPresenter.cachedSources?.sources as ArrayList<SourceItem?>)
        Mockito.verify(mockSourcesView).disableContinueFooter()
        Mockito.verify(mockSourcesView).showContinueFooter()
        Mockito.verify(mockSourcesView).enableContinueFooter()
    }

    /**
     * Test if the [SourcesPresenter.searchFilterChanged] method is executed successfully and
     * works as expected in a scenario where filter text is empty and cached sources are unavailable.
     */
    @Test
    fun testSourcesPresenterSearchFilterChangeFilterEmptyCachedSourcesUnavailable() {
        // Just attach the view to the presenter.
        sourcesPresenter.onAttach(mockSourcesView)

        val filter = ""

        sourcesPresenter.searchFilterChanged(filter)
    }

    /**
     * Test if the [SourcesPresenter.searchFilterChanged] method is executed successfully and
     * works as expected in a scenario where filter text is not empty and cached sources are
     * unavailable.
     */
    @Test
    fun testSourcesPresenterSearchFilterChangeFilterNotEmptyCachedSourcesUnavailable() {
        // Just attach the view to the presenter.
        sourcesPresenter.onAttach(mockSourcesView)

        val filter = "ars"

        sourcesPresenter.searchFilterChanged(filter)

        Mockito.verify(mockSourcesView).showError("No source(s) available to search")
        Mockito.verify(mockSourcesView).disableContinueFooter()
        Mockito.verify(mockSourcesView).hideContinueFooter()
    }

    /**
     * Test if the [SourcesPresenter.refresh] method is executed successfully and works as
     * expected in a scenario where no filter was applied.
     */
    @Test
    fun testSourcesPresenterRefreshNoFilterSuccess() {
        // Just attach the view to the presenter.
        sourcesPresenter.onAttach(mockSourcesView)

        // Mock internet helper and return true for internet connectivity checks.
        Mockito.doReturn(true)
                .`when`(mockInternetHelper)
                .isAvailable()

        // Mock NewsApiService and return a dummy sources data when sources method is called.
        val sourceItems = arrayListOf(
                SourceItem("ars-technica-1",
                        "Ars Technica 1",
                        "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                        "http://arstechnica.com",
                        "technology",
                        "en",
                        "us"),
                SourceItem("ars-technica-2",
                        "Ars Technica 2",
                        "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                        "http://arstechnica.com",
                        "technology",
                        "en",
                        "us"),
                SourceItem("ars-technica-3",
                        "Ars Technica 3",
                        "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                        "http://arstechnica.com",
                        "technology",
                        "en",
                        "us"))
        val sources = Sources("ok", sourceItems)
        sourcesPresenter.cachedSources = sources.copy()

        sourcesPresenter.selectedCachedSourcesMap["ars-technica-1"] = SourceItem("ars-technica",
                "Ars Technica 1",
                "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                "http://arstechnica.com",
                "technology",
                "en",
                "us")
        sourcesPresenter.selectedCachedSourcesMap["ars-technica-2"] = SourceItem("ars-technica",
                "Ars Technica 2",
                "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                "http://arstechnica.com",
                "technology",
                "en",
                "us")
        sourcesPresenter.selectedCachedSourcesMap["ars-technica-3"] = SourceItem("ars-technica",
                "Ars Technica 3",
                "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                "http://arstechnica.com",
                "technology",
                "en",
                "us")

        Mockito.doReturn(Single.just(sources))
                .`when`(mockNewsApiService)
                .sources()

        Mockito.doReturn("")
                .`when`(mockSourcesView)
                .getSearchFilter()

        sourcesPresenter.refresh()

        testScheduler.triggerActions()

        Mockito.verify(mockSourcesView).disableSearch()
        Mockito.verify(mockSourcesView).disableContinueFooter()
        Mockito.verify(mockSourcesView).hideError()
        Mockito.verify(mockSourcesView).showRefreshingDoneMessage("News refreshed")
        Mockito.verify(mockSourcesView).stopRefreshing()
        Mockito.verify(mockSourcesView).clearSources()
        Mockito.verify(mockSourcesView).showSources(sourcesPresenter.cachedSources?.sources as ArrayList<SourceItem?>)
        Mockito.verify(mockSourcesView).enableSearch()
        Mockito.verify(mockSourcesView).showContinueFooter()
        Mockito.verify(mockSourcesView).enableContinueFooter()
    }

    /**
     * Test if the [SourcesPresenter.refresh] method is executed successfully and works as
     * expected in a scenario where filter was applied.
     */
    @Test
    fun testSourcesPresenterRefreshFilterSuccess() {
        // Just attach the view to the presenter.
        sourcesPresenter.onAttach(mockSourcesView)

        // Mock internet helper and return true for internet connectivity checks.
        Mockito.doReturn(true)
                .`when`(mockInternetHelper)
                .isAvailable()

        // Mock NewsApiService and return a dummy sources data when sources method is called.
        val sourceItems = arrayListOf(
                SourceItem("ars-technica-1",
                        "Ars Technica 1",
                        "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                        "http://arstechnica.com",
                        "technology",
                        "en",
                        "us"),
                SourceItem("ars-technica-2",
                        "Ars Technica 2",
                        "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                        "http://arstechnica.com",
                        "technology",
                        "en",
                        "us"),
                SourceItem("ars-technica-3",
                        "Ars Technica 3",
                        "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                        "http://arstechnica.com",
                        "technology",
                        "en",
                        "us"))
        val sources = Sources("ok", sourceItems)
        sourcesPresenter.cachedSources = sources.copy()

        sourcesPresenter.selectedCachedSourcesMap["ars-technica-1"] = SourceItem("ars-technica",
                "Ars Technica 1",
                "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                "http://arstechnica.com",
                "technology",
                "en",
                "us")
        sourcesPresenter.selectedCachedSourcesMap["ars-technica-2"] = SourceItem("ars-technica",
                "Ars Technica 2",
                "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                "http://arstechnica.com",
                "technology",
                "en",
                "us")
        sourcesPresenter.selectedCachedSourcesMap["ars-technica-3"] = SourceItem("ars-technica",
                "Ars Technica 3",
                "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                "http://arstechnica.com",
                "technology",
                "en",
                "us")

        Mockito.doReturn(Single.just(sources))
                .`when`(mockNewsApiService)
                .sources()

        Mockito.doReturn("ars")
                .`when`(mockSourcesView)
                .getSearchFilter()

        sourcesPresenter.refresh()

        testScheduler.triggerActions()

        Mockito.verify(mockSourcesView).disableSearch()
        Mockito.verify(mockSourcesView).disableContinueFooter()
        Mockito.verify(mockSourcesView).hideError()
        Mockito.verify(mockSourcesView).showRefreshingDoneMessage("News refreshed")
        Mockito.verify(mockSourcesView).stopRefreshing()
        Mockito.verify(mockSourcesView).clearSources()
        Mockito.verify(mockSourcesView).showSources(sourcesPresenter.cachedSources?.sources as ArrayList<SourceItem?>)
        Mockito.verify(mockSourcesView).enableSearch()
        Mockito.verify(mockSourcesView).showContinueFooter()
        Mockito.verify(mockSourcesView).enableContinueFooter()
    }

    /**
     * Test if the [SourcesPresenter.refresh] method is executed successfully and works as
     * expected in a scenario where invalid filter was applied.
     */
    @Test
    fun testSourcesPresenterRefreshFilterFailure() {
        // Just attach the view to the presenter.
        sourcesPresenter.onAttach(mockSourcesView)

        // Mock internet helper and return true for internet connectivity checks.
        Mockito.doReturn(true)
                .`when`(mockInternetHelper)
                .isAvailable()

        // Mock NewsApiService and return a dummy sources data when sources method is called.
        val sourceItems = arrayListOf(
                SourceItem("ars-technica-1",
                        "Ars Technica 1",
                        "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                        "http://arstechnica.com",
                        "technology",
                        "en",
                        "us"),
                SourceItem("ars-technica-2",
                        "Ars Technica 2",
                        "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                        "http://arstechnica.com",
                        "technology",
                        "en",
                        "us"),
                SourceItem("ars-technica-3",
                        "Ars Technica 3",
                        "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                        "http://arstechnica.com",
                        "technology",
                        "en",
                        "us"))
        val sources = Sources("ok", sourceItems)
        sourcesPresenter.cachedSources = sources.copy()

        sourcesPresenter.selectedCachedSourcesMap["ars-technica-1"] = SourceItem("ars-technica",
                "Ars Technica 1",
                "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                "http://arstechnica.com",
                "technology",
                "en",
                "us")
        sourcesPresenter.selectedCachedSourcesMap["ars-technica-2"] = SourceItem("ars-technica",
                "Ars Technica 2",
                "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                "http://arstechnica.com",
                "technology",
                "en",
                "us")
        sourcesPresenter.selectedCachedSourcesMap["ars-technica-3"] = SourceItem("ars-technica",
                "Ars Technica 3",
                "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                "http://arstechnica.com",
                "technology",
                "en",
                "us")

        Mockito.doReturn(Single.just(sources))
                .`when`(mockNewsApiService)
                .sources()

        Mockito.doReturn("BBC")
                .`when`(mockSourcesView)
                .getSearchFilter()

        sourcesPresenter.refresh()

        testScheduler.triggerActions()

        Mockito.verify(mockSourcesView).disableSearch()
        Mockito.verify(mockSourcesView).disableContinueFooter()
        Mockito.verify(mockSourcesView).showErrorToast("No such source(s) available")
        Mockito.verify(mockSourcesView).stopRefreshing()
        Mockito.verify(mockSourcesView).enableSearch()
        Mockito.verify(mockSourcesView).enableContinueFooter()
    }

    /**
     * Test if the [SourcesPresenter.refresh] method is executed successfully and works as
     * expected in a scenario where internet is not available.
     */
    @Test
    fun testSourcesPresenterRefreshInternetFailure() {
        // Just attach the view to the presenter.
        sourcesPresenter.onAttach(mockSourcesView)

        // Mock internet helper and return true for internet connectivity checks.
        Mockito.doReturn(false)
                .`when`(mockInternetHelper)
                .isAvailable()

        sourcesPresenter.selectedCachedSourcesMap["ars-technica-1"] = SourceItem("ars-technica",
                "Ars Technica 1",
                "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                "http://arstechnica.com",
                "technology",
                "en",
                "us")
        sourcesPresenter.selectedCachedSourcesMap["ars-technica-2"] = SourceItem("ars-technica",
                "Ars Technica 2",
                "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                "http://arstechnica.com",
                "technology",
                "en",
                "us")
        sourcesPresenter.selectedCachedSourcesMap["ars-technica-3"] = SourceItem("ars-technica",
                "Ars Technica 3",
                "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                "http://arstechnica.com",
                "technology",
                "en",
                "us")

        sourcesPresenter.refresh()

        Mockito.verify(mockSourcesView).showErrorToast("No internet available")
        Mockito.verify(mockSourcesView).stopRefreshing()
        Mockito.verify(mockSourcesView).enableSearch()
        Mockito.verify(mockSourcesView).enableContinueFooter()
    }

    /**
     * Test if the [SourcesPresenter.refresh] method is executed successfully and works as
     * expected in a scenario where remote server is unable to fetch the sources.
     */
    @Test
    fun testSourcesPresenterRefreshApiFailure() {
        // Just attach the view to the presenter.
        sourcesPresenter.onAttach(mockSourcesView)

        // Mock internet helper and return true for internet connectivity checks.
        Mockito.doReturn(true)
                .`when`(mockInternetHelper)
                .isAvailable()

        sourcesPresenter.selectedCachedSourcesMap["ars-technica-1"] = SourceItem("ars-technica",
                "Ars Technica 1",
                "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                "http://arstechnica.com",
                "technology",
                "en",
                "us")
        sourcesPresenter.selectedCachedSourcesMap["ars-technica-2"] = SourceItem("ars-technica",
                "Ars Technica 2",
                "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                "http://arstechnica.com",
                "technology",
                "en",
                "us")
        sourcesPresenter.selectedCachedSourcesMap["ars-technica-3"] = SourceItem("ars-technica",
                "Ars Technica 3",
                "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                "http://arstechnica.com",
                "technology",
                "en",
                "us")

        val singleSourcesError = Single.create<Sources> {
            it.onError(Exception(""))
        }

        Mockito.doReturn(singleSourcesError)
                .`when`(mockNewsApiService)
                .sources()

        sourcesPresenter.refresh()

        testScheduler.triggerActions()

        Mockito.verify(mockSourcesView).disableSearch()
        Mockito.verify(mockSourcesView).disableContinueFooter()
        Mockito.verify(mockSourcesView).showErrorToast("")
        Mockito.verify(mockSourcesView).stopRefreshing()
        Mockito.verify(mockSourcesView).enableSearch()
        Mockito.verify(mockSourcesView).enableContinueFooter()
    }

    /**
     * Test if the [SourcesPresenter.refresh] method is executed successfully and works as
     * expected in a scenario where remote server is able to fetch the sources but the sources
     * returned from server is malformed.
     */
    @Test
    fun testSourcesPresenterRefreshApiFailureAlternative() {
        // Just attach the view to the presenter.
        sourcesPresenter.onAttach(mockSourcesView)

        // Mock internet helper and return true for internet connectivity checks.
        Mockito.doReturn(true)
                .`when`(mockInternetHelper)
                .isAvailable()

        val sources = Sources("failure", null)

        sourcesPresenter.selectedCachedSourcesMap["ars-technica-1"] = SourceItem("ars-technica",
                "Ars Technica 1",
                "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                "http://arstechnica.com",
                "technology",
                "en",
                "us")
        sourcesPresenter.selectedCachedSourcesMap["ars-technica-2"] = SourceItem("ars-technica",
                "Ars Technica 2",
                "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                "http://arstechnica.com",
                "technology",
                "en",
                "us")
        sourcesPresenter.selectedCachedSourcesMap["ars-technica-3"] = SourceItem("ars-technica",
                "Ars Technica 3",
                "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
                "http://arstechnica.com",
                "technology",
                "en",
                "us")

        Mockito.doReturn(Single.just(sources))
                .`when`(mockNewsApiService)
                .sources()

        Mockito.doReturn("")
                .`when`(mockSourcesView)
                .getSearchFilter()

        sourcesPresenter.refresh()

        testScheduler.triggerActions()

        Mockito.verify(mockSourcesView).disableSearch()
        Mockito.verify(mockSourcesView).disableContinueFooter()
        Mockito.verify(mockSourcesView).showErrorToast("No such source(s) available")
        Mockito.verify(mockSourcesView).stopRefreshing()
        Mockito.verify(mockSourcesView).enableSearch()
        Mockito.verify(mockSourcesView).enableContinueFooter()
    }

    @After
    fun tearDown() {
        sourcesPresenter.onDetach()
    }
}