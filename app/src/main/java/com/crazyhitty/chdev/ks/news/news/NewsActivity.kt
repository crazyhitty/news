package com.crazyhitty.chdev.ks.news.news

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.crazyhitty.chdev.ks.news.R
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {
    private val newsRecyclerAdapter: NewsRecyclerAdapter = NewsRecyclerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        setupNewsRecyclerView()
    }

    private fun setupNewsRecyclerView() {
        recyclerViewNews.layoutManager = LinearLayoutManager(applicationContext)
        recyclerViewNews.adapter = newsRecyclerAdapter
    }
}