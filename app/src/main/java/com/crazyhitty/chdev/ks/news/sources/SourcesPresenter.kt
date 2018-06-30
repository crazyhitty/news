package com.crazyhitty.chdev.ks.news.sources

import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.crazyhitty.chdev.ks.news.base.Presenter
import com.crazyhitty.chdev.ks.news.data.DataStore
import com.crazyhitty.chdev.ks.news.data.api.NewsApiService
import com.crazyhitty.chdev.ks.news.data.api.model.news.SourceItem
import com.crazyhitty.chdev.ks.news.data.api.model.news.Sources
import com.crazyhitty.chdev.ks.news.util.internet.InternetHelper
import com.crazyhitty.chdev.ks.news.util.rx.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import java.util.regex.Pattern
import javax.inject.Inject

/**
 * Implementation of [SourcesContract.Presenter]. The main job of this presenter is to fetch list
 * of sources from remote server and manage selected sources.
 *
 * @param internetHelper        [InternetHelper]'s instance to monitor internet availability.
 * @param schedulerProvider     [SchedulerProvider]'s instance to run code on appropriate threads.
 * @param compositeDisposable   [CompositeDisposable]'s instance for managing multiple disposables.
 * @param newsApiService        [NewsApiService]'s instance for fetching sources data from remote
 *                              server.
 * @param dataStore             [DataStore]'s instance for caching sources so it can be used later.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class SourcesPresenter @Inject constructor(private val internetHelper: InternetHelper,
                                           private val schedulerProvider: SchedulerProvider,
                                           private val compositeDisposable: CompositeDisposable,
                                           private val newsApiService: NewsApiService,
                                           private val dataStore: DataStore) :
        Presenter<SourcesContract.View>(), SourcesContract.Presenter {
    private val log = AnkoLogger(this::class.java)

    /**
     * Stores a copy of sources fetched from remote server.
     */
    var cachedSources: Sources? = null

    var selectedCachedSourcesMap: HashMap<String?, SourceItem?> = HashMap()

    override fun onAttach(view: SourcesContract.View) {
        super.onAttach(view)
        // Check if internet is available or not.
        if (internetHelper.isAvailable()) {
            this.view.showProgress()
            this.view.disableSearch()
            this.view.disableRefresh()
            this.view.disableContinueFooter()
            this.view.hideContinueFooter()
            compositeDisposable.add(newsApiService.sources()
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .map {
                        it.sources?.map {
                            it?.spannableName = SpannableString(it?.name)
                        }
                        // Return the sources with added spannable names with each item.
                        it
                    }
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
                            this.view.showContinueFooter()
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

        if (filter.isEmpty()) {
            view.hideClearSearchButton()
        } else {
            view.showClearSearchButton()
        }

        if (cachedSources == null ||
                cachedSources?.sources == null ||
                cachedSources?.sources?.isEmpty() == true) {
            if (!filter.isBlank()) {
                view.showError("No source(s) available to search")
                view.disableContinueFooter()
                view.hideContinueFooter()
            }
        } else {
            cachedSources?.let {
                val sources = it
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
                                    view.disableContinueFooter()
                                    view.showContinueFooter()
                                    if (selectedCachedSourcesMap.size >= 3) {
                                        view.enableContinueFooter()
                                    }
                                },
                                {
                                    // Handle failure scenario.
                                    view.clearSources()
                                    view.showError("No such source(s) available")
                                    view.disableContinueFooter()
                                    view.hideContinueFooter()
                                }
                        ))
            }
        }
    }

    override fun refresh() {
        // Check if internet is available or not.
        if (internetHelper.isAvailable()) {
            view.disableSearch()
            view.disableContinueFooter()
            compositeDisposable.add(newsApiService.sources()
                    .map {
                        // Check if any selection needs to be performed in the new sources data.
                        selectSources(selectedCachedSourcesMap, it)

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
                            view.showContinueFooter()
                            if (selectedCachedSourcesMap.size >= 3) {
                                view.enableContinueFooter()
                            }
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
                            if (selectedCachedSourcesMap.size >= 3) {
                                view.enableContinueFooter()
                            }
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
                        if (selectedCachedSourcesMap.size >= 3) {
                            view.enableContinueFooter()
                        }
                    }))
        } else {
            view.showErrorToast("No internet available")
            view.stopRefreshing()
            view.enableSearch()
            if (selectedCachedSourcesMap.size >= 3) {
                view.enableContinueFooter()
            }
        }
    }

    override fun clearSearchButtonClicked() {
        view.clearSearchFilter()
        cachedSources?.sources?.takeIf { it.isNotEmpty() }
                .let {
                    view.clearSources()
                    view.hideError()
                    view.showSources(it?.toMutableList() as ArrayList<SourceItem?>)
                }
    }

    override fun sourceItemCheckClicked(sourceItem: SourceItem?) {
        if (sourceItem?.selected == true) {
            selectedCachedSourcesMap[sourceItem.id] = sourceItem
        } else {
            selectedCachedSourcesMap.remove(sourceItem?.id)
        }

        // Enable/disable continue footer if the current number of selected items are greater/less
        // than 3.
        if (selectedCachedSourcesMap.size >= 3) {
            view.enableContinueFooter()
        } else {
            view.disableContinueFooter()
        }
    }

    override fun continueFooterClicked() {
        if (selectedCachedSourcesMap.size >= 3) {
            // Disable the continue footer.
            view.disableContinueFooter()

            // Save the current sources asynchronously.
            compositeDisposable.add(
                    Completable.create({
                        val selectedSources = Sources("ok",
                                selectedCachedSourcesMap.values.toList())
                        dataStore.saveSources(selectedSources)
                        dataStore.sourcesSelectionComplete(true)
                        it.onComplete()
                    })
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .subscribe {
                                view.redirectToNewsScreen()
                            }
            )
        } else {
            view.showErrorToast("Please select at least 3 sources to continue")
        }
    }

    /**
     * Filter sources with specific name.
     *
     * @param filter    Search filter
     * @param sources   Current sources data to be filtered
     */
    fun filterSources(filter: String, sources: Sources): Sources {
        if (filter.isBlank()) {
            val filteredSources = sources.sources?.map {
                it?.spannableName = SpannableString(it?.name)
                // Return the filtered sources with spannable name.
                it
            }
            return sources.copy(sources = filteredSources)
        }

        val filteredSources = sources.sources?.filter {
            it?.name?.contains(filter, true) == true
        }?.map {
            val pattern = Pattern.compile(filter, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(it?.name)

            val spannableName = SpannableString(it?.name)

            // The loop will keep on executing until matcher finds all the matches. This will help
            // us get the start/end indexes for matched substrings.
            while (matcher.find()) {
                spannableName.setSpan(ForegroundColorSpan(Color.RED),
                        matcher.start(),
                        matcher.end(),
                        0)
            }

            it?.spannableName = spannableName
            // Return the modified source item.
            it
        }
        return sources.copy(sources = filteredSources)
    }

    fun selectSources(selectedSourcesMap: HashMap<String?, SourceItem?>, newSources: Sources) {
        newSources.sources?.map {
            val selected = selectedSourcesMap.containsKey(it?.id)
            it?.selected = selected
        }
    }
}