package com.crazyhitty.chdev.ks.news.sources

import android.os.Bundle
import com.crazyhitty.chdev.ks.news.base.BasePresenter
import com.crazyhitty.chdev.ks.news.base.BaseView
import com.crazyhitty.chdev.ks.news.data.api.model.news.SourceItem

/**
 * Contains blueprint for View and Presenter responsible for showing and getting sources.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
interface SourcesContract {
    interface View : BaseView {
        /**
         * Enable search UI.
         */
        fun enableSearch()

        /**
         * Disable search UI.
         */
        fun disableSearch()

        /**
         * Hide the clear button in the UI.
         */
        fun showClearSearchButton()

        /**
         * Show the clear button in the UI.
         */
        fun hideClearSearchButton()

        /**
         * Enable refresh UI, so that user can refresh the sources.
         */
        fun enableRefresh()

        /**
         * Disallow the refresh UI to trigger.
         */
        fun disableRefresh()

        /**
         * Show the progress indicating loading of sources.
         */
        fun showProgress()

        /**
         * Hide the progress indicating loading of sources.
         */
        fun hideProgress()

        /**
         * Provide current search filter text.
         *
         * @return
         * Current search filter string.
         */
        fun getSearchFilter(): String

        /**
         * Clear any search text currently being shown on the UI.
         */
        fun clearSearchFilter()

        /**
         * Show sourceItems on the UI.
         *
         * @param sourceItems   List of sourceItems to be shown.
         */
        fun showSources(sourceItems: ArrayList<SourceItem?>)

        /**
         * Clear the sources currently being displayed.
         */
        fun clearSources()

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
         * Show continue footer on the UI.
         */
        fun showContinueFooter()

        /**
         * Hide continue footer on the UI.
         */
        fun hideContinueFooter()

        /**
         * Enable continue footer on the UI.
         */
        fun enableContinueFooter()

        /**
         * Disable continue footer on the UI.
         */
        fun disableContinueFooter()

        /**
         * Redirect the user to news listing screen.
         */
        fun redirectToNewsScreen()
    }

    interface Presenter : BasePresenter<View> {
        /**
         * Should be called when search filter is modified.
         *
         * @param filter    Search filter
         */
        fun searchFilterChanged(filter: String)

        /**
         * Refreshes the sources.
         */
        fun refresh()

        /**
         * Should be called when search clear button click is invoked in the UI.
         */
        fun clearSearchButtonClicked()

        /**
         * Should be called when a particular source item check button is clicked.
         *
         * @param sourceItem    Checked source item
         */
        fun sourceItemCheckClicked(sourceItem: SourceItem?)

        /**
         * Should be called when continue footer is clicked.
         */
        fun continueFooterClicked()
    }
}