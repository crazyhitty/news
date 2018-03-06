package com.crazyhitty.chdev.ks.news.sources

import com.crazyhitty.chdev.ks.news.base.Presenter
import com.crazyhitty.chdev.ks.news.data.api.NewsApiService
import com.crazyhitty.chdev.ks.news.data.api.model.news.SourceItem
import com.crazyhitty.chdev.ks.news.data.api.model.news.Sources
import com.crazyhitty.chdev.ks.news.util.internet.InternetHelper
import com.crazyhitty.chdev.ks.news.util.rx.SchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import javax.inject.Inject

/**
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class SourcesPresenter @Inject constructor(private val internetHelper: InternetHelper,
                                           private val schedulerProvider: SchedulerProvider,
                                           private val compositeDisposable: CompositeDisposable,
                                           private val newsApiService: NewsApiService) :
        Presenter<SourcesContract.View>(), SourcesContract.Presenter {
    private val log = AnkoLogger(this::class.java)

    /**
     * Stores a copy of sources fetched from remote server.
     */
    private var cachedSources: Sources? = null

    override fun onAttach(view: SourcesContract.View) {
        super.onAttach(view)
        // Check if internet is available or not.
        if (internetHelper.isAvailable()) {
            this.view.showProgress()
            this.view.disableSearch()
            this.view.disableRefresh()
            compositeDisposable.add(newsApiService.sources()
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe({
                        // Handle success scenario.
                        log.info { "Sources loaded from remote server" }

                        // Check if sources status is ok and if list of sources are available.
                        if (it?.status.equals("ok") &&
                                it?.sources?.isNotEmpty() == true) {
                            // Save the items in cache so that they can be filtered later on.
                            cachedSources = it.copy(sources = it.sources.toMutableList())
                            this.view.hideProgress()
                            this.view.showSources(it.sources as ArrayList<SourceItem?>)
                            this.view.enableSearch()
                            this.view.enableRefresh()
                        } else {
                            log.error {
                                """
                                    Unable to display sources
                                    Cause:
                                    status=${it?.status}
                                    sourcesSize=${it?.sources?.size}
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
                                Unable to load sources from remote server
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

    override fun searchFilterChanged(filter: String) {
        log.info { "Filter text: $filter" }
        if (cachedSources == null ||
                cachedSources?.sources == null ||
                cachedSources?.sources?.isEmpty() == true) {
            if (!filter.isBlank()) {
                view.showError("No source(s) available to search")
            }
        } else {
            cachedSources?.let {
                val sources = it
                if (!filter.isBlank()) {
                    compositeDisposable.add(Single.create<Sources> {
                        val filteredSources = filterSources(filter, sources)
                        if (filteredSources.sources?.isEmpty() == true) {
                            it.onError(NullPointerException("No such source(s) available"))
                        } else {
                            it.onSuccess(filteredSources)
                        }
                    }.subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .subscribe(
                                    {
                                        // Handle success scenario.
                                        view.clearSources()
                                        view.hideError()
                                        view.showSources(it.sources as ArrayList<SourceItem?>)
                                    },
                                    {
                                        // Handle failure scenario.
                                        view.clearSources()
                                        view.showError("No such source(s) available")
                                    }
                            ))
                }
            }
        }
    }

    override fun refresh() {
        // Check if internet is available or not.
        if (internetHelper.isAvailable()) {
            view.disableSearch()
            compositeDisposable.add(newsApiService.sources()
                    .map {
                        // Save the items in cache so that they can be filtered later on.
                        cachedSources = it.copy(sources = it.sources?.toMutableList())
                        filterSources(view.getSearchFilter(), it)
                    }
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe({
                        // Handle success scenario.
                        log.info { "Sources loaded from remote server" }

                        // Check if news status is ok and if news articles are available.
                        if (it?.status.equals("ok") &&
                                it?.sources?.isNotEmpty() == true) {
                            view.hideError()
                            view.showRefreshingDoneMessage("News refreshed")
                            view.stopRefreshing()
                            view.clearSources()
                            view.showSources(it.sources as ArrayList<SourceItem?>)
                            view.enableSearch()
                        } else {
                            log.error {
                                """
                                    Unable to display sources
                                    Cause:
                                    status=${it?.status}
                                    sourcesSize=${it?.sources?.size}
                                """.trimIndent()
                            }
                            view.showErrorToast("No such source(s) available")
                            view.stopRefreshing()
                            view.enableSearch()
                        }
                    }, {
                        // Handle failure scenario.
                        log.error {
                            """
                                Unable to load sources from remote server
                                Cause:
                                $it
                            """.trimIndent()
                        }
                        view.showErrorToast(it.message ?: "Unknown error")
                        view.stopRefreshing()
                        view.enableSearch()
                    }))
        } else {
            view.showErrorToast("No internet available")
            view.stopRefreshing()
            view.enableSearch()
        }
    }

    override fun clearSearchButtonClicked() {
        view.clearSearchFilter()
        cachedSources?.sources?.takeIf { it.isNotEmpty() }
                .let { view.showSources(it as ArrayList<SourceItem?>) }
    }

    /**
     * Filter sources with specific name.
     *
     * @param filter    Search filter
     * @param sources   Current sources data to be filtered
     */
    private fun filterSources(filter: String, sources: Sources): Sources {
        val filteredSources = sources.sources?.filter {
            it?.name?.contains(filter, true) == true
        }
        return sources.copy(sources = filteredSources)
    }
}