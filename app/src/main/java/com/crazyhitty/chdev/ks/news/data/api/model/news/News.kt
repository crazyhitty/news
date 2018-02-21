package com.crazyhitty.chdev.ks.news.data.api.model.news

data class News(
        val totalResults: Int? = null,
        val articles: List<ArticlesItem?>? = null,
        val status: String? = null
)
