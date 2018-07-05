package com.crazyhitty.chdev.ks.news.data.newslisting

import com.crazyhitty.chdev.ks.news.data.base.DataToDomainMapper
import com.crazyhitty.chdev.ks.news.domain.newslisting.Article
import com.crazyhitty.chdev.ks.news.domain.newslisting.News
import com.crazyhitty.chdev.ks.news.domain.newslisting.Source
import javax.inject.Inject

class NewsMapper @Inject constructor() : DataToDomainMapper<NewsDto, News> {
    override fun transform(dataModel: NewsDto) = News(
            status = dataModel.status,
            totalResults = dataModel.totalResults,
            articles = dataModel.articles.map { transformArticleDto(it) }
    )

    private fun transformArticleDto(dataModel: ArticleDto) = Article(
            source = transformSourceDto(dataModel.source),
            author = dataModel.author,
            title = dataModel.title,
            description = dataModel.description,
            url = dataModel.url,
            urlToImage = dataModel.urlToImage ?: "",
            publishedAt = dataModel.publishedAt
    )

    private fun transformSourceDto(dataModel: SourceDto) = Source(
            id = dataModel.id,
            name = dataModel.name
    )
}