package com.crazyhitty.chdev.ks.news.presentation.newslisting

data class NewsViewModel(
        val status: String,
        val totalResults: Int,
        val articles: List<ArticleViewModel>
)

data class ArticleViewModel(
        val id: String,
        val title: String,
        val description: String,
        val author: String,
        val publishedAt: String,
        val urlToImage: String
)