package com.crazyhitty.chdev.ks.news.newsListing

import android.os.Bundle
import com.crazyhitty.chdev.ks.news.R
import com.crazyhitty.chdev.ks.news.base.BaseAppCompatActivity
import kotlinx.android.synthetic.main.activity_news_listing.*
import javax.inject.Inject

/**
 * This activity will display the list of news for every source user has selected.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class NewsListingActivity : BaseAppCompatActivity() {
    @Inject
    lateinit var newsListingViewPagerAdapter: NewsListingViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_listing)

        // Initialize dependency.
        activityComponent.inject(this)

        setViewPager()
    }

    private fun setViewPager() {
        viewPagerNews.adapter = newsListingViewPagerAdapter

        // TODO: testing code, remove it after testing UI.
        newsListingViewPagerAdapter.sources = arrayOf("ars-technica", "ars-technica")
        newsListingViewPagerAdapter.notifyDataSetChanged()
    }
}