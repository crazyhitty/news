package com.crazyhitty.chdev.ks.news.data.newslisting

import com.squareup.moshi.Json

data class NewsDto(
        @Json(name = "status") val status: String,
        @Json(name = "totalResults") val totalResults: Int,
        @Json(name = "articles") val articles: List<ArticleDto>
)

data class ArticleDto(
        @Json(name = "source") val source: SourceDto,
        @Json(name = "author") val author: String,
        @Json(name = "title") val title: String,
        @Json(name = "description") val description: String,
        @Json(name = "url") val url: String,
        @Json(name = "urlToImage") val urlToImage: String?,
        @Json(name = "publishedAt") val publishedAt: String
)

data class SourceDto(
        @Json(name = "id") val id: String,
        @Json(name = "name") val name: String
)