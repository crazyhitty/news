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
        recyclerViewNews.layoutManager = linearLayoutManager
        recyclerViewNews.adapter = newsRecyclerAdapter

        newsRecyclerAdapter.onItemClickListener = {
            newsListingPresenter.newsItemClicked(Bundle(), it)
        }

        newsRecyclerAdapter.onErrorViewClickListener = {
            newsListingPresenter.recyclerLoadMoreErrorViewClicked()
        }
    }

    private fun setupRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener {
            newsListingPresenter.refresh()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
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
        toast("Not yet implemented!!")
    }

    override fun clearNews() {
        newsRecyclerAdapter.clear()
    }

    override fun stopRefreshing() {
        swipeRefreshLayout.isRefreshing = false
    }

    override fun showRefreshingDoneMessage(message: String) {
        toast(message)
    }

    override fun hideError() {
        textViewNewsUnavailable.visibility = View.GONE
    }

    override fun showError(message: String) {
        textViewNewsUnavailable.text = message.plus("\nSwipe down to refresh")
        textViewNewsUnavailable.visibility = View.VISIBLE
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
        recyclerViewNews.addOnScrollListener(object : RecyclerScrollListener(linearLayoutManager, 6) {
            override fun onPositionAppeared(position: Int) {
                super.onPositionAppeared(position)
                log.info { "Last 5th news article item is visible on the screen with position($position)" }
                newsListingPresenter.reachedLastFifthNewsItem()
            }
        })
    }

    override fun stopListeningForLastFifthNewsItemShown() {
        recyclerViewNews.addOnScrollListener(object : RecyclerView.OnScrollListener(){})
    }
}