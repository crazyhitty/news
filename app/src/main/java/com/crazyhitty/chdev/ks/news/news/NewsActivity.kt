package com.crazyhitty.chdev.ks.news.news

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.crazyhitty.chdev.ks.news.NewsApplication
import com.crazyhitty.chdev.ks.news.R
import com.crazyhitty.chdev.ks.news.data.model.news.News
import com.crazyhitty.chdev.ks.news.di.components.DaggerActivityComponent
import com.crazyhitty.chdev.ks.news.di.modules.ActivityModule
import kotlinx.android.synthetic.main.activity_news.*
import org.jetbrains.anko.toast
import javax.inject.Inject

/**
 * This activity will display the list of news.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class NewsActivity : AppCompatActivity(), NewsContract.View {
    private val activityComponent by lazy {
        DaggerActivityComponent.builder()
                .activityModule(ActivityModule(this))
                .applicationComponent((application as NewsApplication).applicationComponent)
                .build()
    }

    @Inject
    lateinit var newsPresenter: NewsContract.Presenter

    @Inject
    lateinit var newsRecyclerAdapter: NewsRecyclerAdapter

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        // Initialize dependency.
        activityComponent.inject(this)

        setupNewsRecyclerView()

        // Setup presenter.
        newsPresenter.onViewCreated(this)
    }

    private fun setupNewsRecyclerView() {
        recyclerViewNews.layoutManager = linearLayoutManager
        recyclerViewNews.adapter = newsRecyclerAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        newsPresenter.onViewDestroyed()
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