package com.crazyhitty.chdev.ks.news.domain.newslisting

data class News(
        val status: String,
        val totalResults: Int,
        val articles: List<Article>
)

data class Article(
        val source: Source,
        val author: String,
        val title: String,
        val description: String,
        val url: String,
        val urlToImage: String,
        val publishedAt: String
)

data class Source(
        val id: String,
        val name: String
)