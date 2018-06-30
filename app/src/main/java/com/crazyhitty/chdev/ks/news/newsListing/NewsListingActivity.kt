package com.crazyhitty.chdev.ks.news.newsListing

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.crazyhitty.chdev.ks.news.R
import com.crazyhitty.chdev.ks.news.base.BaseAppCompatActivity
import com.crazyhitty.chdev.ks.news.data.api.model.news.ArticleItem
import kotlinx.android.synthetic.main.activity_news_listing.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import javax.inject.Inject

/**
 * This activity will display the list of news.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class NewsListingActivity : BaseAppCompatActivity(), NewsListingContract.View {
    private val log = AnkoLogger(this::class.java)

    @Inject
    lateinit var newsListingPresenter: NewsListingContract.Presenter

    @Inject
    lateinit var newsRecyclerAdapter: NewsRecyclerAdapter

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_listing)

        // Initialize dependency.
        activityComponent.inject(this)

        setupNewsRecyclerView()

        setupRefreshLayout()

        // Setup presenter.
        newsListingPresenter.onAttach(this)
    }

    private fun setupNewsRecyclerView() {
        newsListingView.linearLayoutManager = linearLayoutManager
        newsListingView.recyclerViewNews.layoutManager = linearLayoutManager
        newsListingView.newsRecyclerAdapter = newsRecyclerAdapter
        newsListingView.recyclerViewNews.adapter = newsRecyclerAdapter

        newsRecyclerAdapter.onItemClickListener = {
            newsListingPresenter.newsItemClicked(Bundle(), it)
        }

        newsRecyclerAdapter.onErrorViewClickListener = {
            newsListingPresenter.recyclerLoadMoreErrorViewClicked()
        }
    }

    private fun setupRefreshLayout() {
        newsListingView.onSwipeDownRefresh {
            newsListingPresenter.refresh()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        newsListingPresenter.onDetach()
    }

    override fun enableRefresh() {
        newsListingView.enableSwipeDownToRefresh()
    }

    override fun disableRefresh() {
        newsListingView.disableSwipeDownToRefresh()
    }

    override fun showProgress() {
        newsListingView.showProgress()
    }

    override fun hideProgress() {
        newsListingView.hideProgress()
    }

    override fun showNewsArticles(articles: ArrayList<ArticleItem?>) {
        newsListingView.updateNewsListing(articles)
    }

    override fun openNewsDetailsActivity(bundle: Bundle) {
        toast("Not yet implemented!!")
    }

    override fun clearNews() {
        newsListingView.clearNewsListing()
    }

    override fun stopRefreshing() {
        newsListingView.stopSwipeDownToRefresh()
    }

    override fun showRefreshingDoneMessage(message: String) {
        toast(message)
    }

    override fun hideError() {
        newsListingView.hideErrorText()
    }

    override fun showError(message: String) {
        newsListingView.updateErrorText(message)
        newsListingView.showErrorText()
    }

    override fun showErrorToast(message: String) {
        toast(message)
    }

    override fun showRecyclerLoadMoreErrorView(message: String) {
        newsRecyclerAdapter.showErrorView(message)
    }

    override fun showRecyclerLoadingView() {
        newsRecyclerAdapter.showLoadingView()
    }

    override fun startListeningForLastFifthNewsItemShown() {
        newsListingView.onNewsScrollItemAppeared(6) {
            log.info { "Last 5th news article item is visible on the screen with position($it)" }
            newsListingPresenter.reachedLastFifthNewsItem()
        }
    }

    override fun stopListeningForLastFifthNewsItemShown() {
        newsListingView.stopNewsScrollListener()
    }
}