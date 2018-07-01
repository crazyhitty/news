package com.crazyhitty.chdev.ks.news.newsListing

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.crazyhitty.chdev.ks.news.NewsApplication
import com.crazyhitty.chdev.ks.news.R
import com.crazyhitty.chdev.ks.news.data.api.model.news.ArticleItem
import com.crazyhitty.chdev.ks.news.di.components.DaggerViewGroupComponent
import com.crazyhitty.chdev.ks.news.di.components.ViewGroupComponent
import com.crazyhitty.chdev.ks.news.di.modules.ViewGroupModule
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import javax.inject.Inject

/**
 * This view is responsible for rendering list of news associated with a particular source.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
@SuppressLint("ViewConstructor")
class NewsListingViewGroup(context: Context?,
                           private val source: String) : RelativeLayout(context), NewsListingContract.View {
    companion object {
        /**
         * Position for which load more should be trigger. When this position is reached, the app
         * should start loading next page of news. This position should be from last.
         * For example: If this value is 6, then when the last 5th position is visible on the
         * screen, it would start loading for more items.
         */
        const val POSITION_FROM_LAST = 6
    }

    private val viewGroupComponent: ViewGroupComponent by lazy {
        DaggerViewGroupComponent.builder()
                .viewGroupModule(ViewGroupModule(this))
                .applicationComponent((context?.applicationContext as NewsApplication).applicationComponent)
                .build()
    }

    @Inject
    lateinit var newsListingViewGroupLayoutParams: ViewGroup.LayoutParams
    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager
    @Inject
    lateinit var newsRecyclerAdapter: NewsRecyclerAdapter
    @Inject
    lateinit var newsListingPresenter: NewsListingContract.Presenter

    private lateinit var recyclerViewNews: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var textViewNewsUnavailable: TextView
    private lateinit var progressBar: ProgressBar

    init {
        // Inject view group component.
        viewGroupComponent.inject(this)

        // Inflate layout.
        val layoutInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutInflater.inflate(R.layout.view_news_listing, this)

        // Set layout params for this FrameLayout.
        layoutParams = newsListingViewGroupLayoutParams

        bindViews()

        setupNewsRecyclerView()

        setupRefreshLayout()

        // Setup presenter.
        newsListingPresenter.onAttach(this)
    }

    /**
     * Get view references.
     */
    private fun bindViews() {
        recyclerViewNews = find(R.id.recyclerViewNews)
        swipeRefreshLayout = find(R.id.swipeRefreshLayout)
        textViewNewsUnavailable = find(R.id.textViewNewsUnavailable)
        progressBar = find(R.id.progressBar)
    }

    /**
     * Setup news recycler view which will be responsible for displaying list of news.
     */
    private fun setupNewsRecyclerView() {
        recyclerViewNews.layoutManager = linearLayoutManager
        recyclerViewNews.adapter = newsRecyclerAdapter

        newsRecyclerAdapter.onItemClickListener = {
            newsListingPresenter.newsItemClicked(Bundle(), it)
        }

        newsRecyclerAdapter.onErrorViewClickListener = {
            newsListingPresenter.recyclerLoadMoreErrorViewClicked()
        }
    }

    /**
     * Setup swipe to refresh, so that user can swipe down and trigger refresh.
     */
    private fun setupRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener {
            newsListingPresenter.refresh()
        }
    }

    override fun onViewRemoved(child: View?) {
        super.onViewRemoved(child)
        newsListingPresenter.onDetach()
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

    override fun showNewsArticles(articles: ArrayList<ArticleItem?>) {
        newsRecyclerAdapter.articles = articles
    }

    override fun openNewsDetailsActivity(bundle: Bundle) {
        context.toast("Not yet implemented!!")
    }

    override fun clearNews() {
        newsRecyclerAdapter.clear()
    }

    override fun stopRefreshing() {
        swipeRefreshLayout.isRefreshing = false
    }

    override fun showRefreshingDoneMessage(message: String) {
        context.toast(message)
    }

    override fun hideError() {
        textViewNewsUnavailable.visibility = View.GONE
    }

    override fun showError(message: String) {
        textViewNewsUnavailable.text = message
        textViewNewsUnavailable.visibility = View.VISIBLE
    }

    override fun showErrorToast(message: String) {
        context.toast(message)
    }

    override fun showRecyclerLoadMoreErrorView(message: String) {
        newsRecyclerAdapter.showErrorView(message)
    }

    override fun showRecyclerLoadingView() {
        newsRecyclerAdapter.showLoadingView()
    }

    override fun startListeningForLastFifthNewsItemShown() {
        recyclerViewNews.addOnScrollListener(object : RecyclerScrollListener(linearLayoutManager, POSITION_FROM_LAST) {
            override fun onPositionAppeared(position: Int) {
                super.onPositionAppeared(position)
                newsListingPresenter.reachedLastFifthNewsItem()
            }
        })
    }

    override fun stopListeningForLastFifthNewsItemShown() {
        recyclerViewNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {})
    }
}