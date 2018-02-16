package com.crazyhitty.chdev.ks.news.newsListing

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.crazyhitty.chdev.ks.news.R
import com.crazyhitty.chdev.ks.news.base.BaseAppCompatActivity
import com.crazyhitty.chdev.ks.news.data.model.news.News
import kotlinx.android.synthetic.main.activity_news_listing.*
import org.jetbrains.anko.toast
import javax.inject.Inject

/**
 * This activity will display the list of news.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class NewsListingActivity : BaseAppCompatActivity(), NewsListingContract.View {
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

        // Setup presenter.
        newsListingPresenter.onViewCreated(this)
    }

    private fun setupNewsRecyclerView() {
        recyclerViewNews.layoutManager = linearLayoutManager
        recyclerViewNews.adapter = newsRecyclerAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        newsListingPresenter.onViewDestroyed()
    }

    override fun showNews(news: News) {
        newsRecyclerAdapter.news = news
    }

    override fun openNewsDetailsActivity(bundle: Bundle) {

    }

    override fun clearNews() {

    }

    override fun showError(message: String) {
        toast(message)
    }
}