package com.crazyhitty.chdev.ks.news.sources

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.crazyhitty.chdev.ks.news.R
import com.crazyhitty.chdev.ks.news.base.BaseAppCompatActivity
import com.crazyhitty.chdev.ks.news.data.api.model.news.SourceItem
import com.crazyhitty.chdev.ks.news.util.extensions.onTextChanged
import kotlinx.android.synthetic.main.activity_sources.*
import org.jetbrains.anko.toast
import javax.inject.Inject

/**
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class SourcesActivity: BaseAppCompatActivity(), SourcesContract.View {
    @Inject
    lateinit var sourcesPresenter: SourcesContract.Presenter

    @Inject
    lateinit var sourcesRecyclerAdapter: SourcesRecyclerAdapter

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sources)

        // Initialize dependency.
        activityComponent.inject(this)

        setupSourcesRecyclerAdapter()

        setupRefreshLayout()

        setupEditTextSources()

        setupImageButtonClear()

        sourcesPresenter.onAttach(this)
    }

    private fun setupSourcesRecyclerAdapter() {
        recyclerViewSources.layoutManager = linearLayoutManager
        recyclerViewSources.adapter = sourcesRecyclerAdapter

        sourcesRecyclerAdapter.onSourceCheckClickListener = {
            sourcesPresenter.sourceItemCheckClicked(it)
        }
    }

    private fun setupRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener {
            sourcesPresenter.refresh()
        }
    }

    private fun setupEditTextSources() {
        editTextSourcesFilter.onTextChanged {
            sourcesPresenter.searchFilterChanged(it)
        }
    }

    private fun setupImageButtonClear() {
        imageButtonClear.setOnClickListener {
            sourcesPresenter.clearSearchButtonClicked()
        }
    }

    override fun enableSearch() {
        editTextSourcesFilter.isEnabled = true
    }

    override fun disableSearch() {
        editTextSourcesFilter.isEnabled = false
    }

    override fun showClearSearchButton() {
        imageButtonClear.visibility = View.VISIBLE
    }

    override fun hideClearSearchButton() {
        imageButtonClear.visibility = View.GONE
    }

    override fun enableRefresh() {
        swipeRefreshLayout.isEnabled = true
    }

    override fun disableRefresh() {
        swipeRefreshLayout.isEnabled = false
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    override fun getSearchFilter() = editTextSourcesFilter.text.toString()

    override fun clearSearchFilter() {
        editTextSourcesFilter.text.clear()
    }

    override fun showSources(sourceItems: ArrayList<SourceItem?>) {
        sourcesRecyclerAdapter.sourceItems = sourceItems
    }

    override fun clearSources() {
        sourcesRecyclerAdapter.clear()
    }

    override fun stopRefreshing() {
        swipeRefreshLayout.isRefreshing = false
    }

    override fun showRefreshingDoneMessage(message: String) {
        toast(message)
    }

    override fun hideError() {
        textViewSourcesUnavailable.visibility = View.GONE
    }

    override fun showError(message: String) {
        textViewSourcesUnavailable.text = message.plus("\nSwipe down to refresh")
        textViewSourcesUnavailable.visibility = View.VISIBLE
    }

    override fun showErrorToast(message: String) {
        toast(message)
    }
}