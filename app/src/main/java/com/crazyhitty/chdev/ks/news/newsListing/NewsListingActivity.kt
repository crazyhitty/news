package com.crazyhitty.chdev.ks.news.newsListing

import android.os.Bundle
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

        newsListingViewGroup.onNewsItemClick {
            newsListingPresenter.newsItemClicked(Bundle(), it)
        }

        newsListingViewGroup.onNewsErrorViewClick {
            newsListingPresenter.recyclerLoadMoreErrorViewClicked()
        }
    }

    private fun setupRefreshLayout() {
        newsListingViewGroup.onSwipeDownRefresh {
            newsListingPresenter.refresh()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        newsListingPresenter.onDetach()
    }

    override fun enableRefresh() {
        newsListingViewGroup.enableSwipeDownToRefresh()
    }

    override fun disableRefresh() {
        newsListingViewGroup.disableSwipeDownToRefresh()
    }

    override fun showProgress() {
        newsListingViewGroup.showProgress()
    }

    override fun hideProgress() {
        newsListingViewGroup.hideProgress()
    }

    override fun showNewsArticles(articles: ArrayList<ArticleItem?>) {
        newsListingViewGroup.updateNewsListing(articles)
    }

    override fun openNewsDetailsActivity(bundle: Bundle) {
        toast("Not yet implemented!!")
    }

    override fun clearNews() {
        newsListingViewGroup.clearNewsListing()
    }

    override fun stopRefreshing() {
        newsListingViewGroup.stopSwipeDownToRefresh()
    }

    override fun showRefreshingDoneMessage(message: String) {
        toast(message)
    }

    override fun hideError() {
        newsListingViewGroup.hideErrorText()
    }

    override fun showError(message: String) {
        newsListingViewGroup.updateErrorText(message)
        newsListingViewGroup.showErrorText()
    }

    override fun showErrorToast(message: String) {
        toast(message)
    }

    override fun showRecyclerLoadMoreErrorView(message: String) {
        newsListingViewGroup.showRecyclerLoadMoreErrorView(message)
    }

    override fun showRecyclerLoadingView() {
        newsListingViewGroup.showRecyclerLoadingView()
    }

    override fun startListeningForLastFifthNewsItemShown() {
        newsListingViewGroup.onNewsScrollItemAppeared(6) {
            log.info { "Last 5th news article item is visible on the screen with position($it)" }
            newsListingPresenter.reachedLastFifthNewsItem()
        }
    }

    override fun stopListeningForLastFifthNewsItemShown() {
        newsListingViewGroup.stopNewsScrollListener()
    }
}