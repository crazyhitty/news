package com.crazyhitty.chdev.ks.news.presentation.newslisting

import android.os.Bundle
import android.view.View
import com.crazyhitty.chdev.ks.news.R
import com.crazyhitty.chdev.ks.news.base.BaseActivity
import kotlinx.android.synthetic.main.activity_news_listing.*
import javax.inject.Inject

/**
 * This activity will display the list of news.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class NewsListingActivity : BaseActivity(), NewsListingContract.View {

    @Inject
    lateinit var presenter: NewsListingPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_listing)

        setupNewsRecyclerView()

        // Setup presenter.
        presenter.onAttach()
    }

    private fun setupNewsRecyclerView() {
        val newsRecyclerAdapter = NewsRecyclerAdapter()
        newsRecyclerAdapter.onItemClickListener = {
            presenter.onNewsItemClicked()
        }
        recyclerViewNews.adapter = newsRecyclerAdapter
    }

    override fun render(newsViewModel: NewsViewModel) {
        (recyclerViewNews.adapter as NewsRecyclerAdapter).articles = newsViewModel.articles.toMutableList()
    }

    override fun showLoading() {
        findViewById<View>(R.id.loadingView).visibility = View.VISIBLE
    }

    override fun hideLoading() {
        findViewById<View>(R.id.loadingView).visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }
}