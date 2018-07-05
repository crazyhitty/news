package com.crazyhitty.chdev.ks.news.presentation.newslisting

import com.crazyhitty.chdev.ks.news.Constants
import com.crazyhitty.chdev.ks.news.base.DomainToViewMapper
import com.crazyhitty.chdev.ks.news.domain.newslisting.Article
import com.crazyhitty.chdev.ks.news.domain.newslisting.News
import com.crazyhitty.chdev.ks.news.util.convertPublishDateToReadable
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class NewsViewModelMapper @Inject constructor() : DomainToViewMapper<News, NewsViewModel> {
    override fun transform(domainModel: News) = NewsViewModel(
            status = domainModel.status,
            totalResults = domainModel.totalResults,
            articles = domainModel.articles.map { transformArticle(it) }
    )

    private fun transformArticle(domainModel: Article) = ArticleViewModel(
            id = domainModel.source.id,
            title = domainModel.title,
            description = domainModel.description,
            author = domainModel.author,
            publishedAt = convertPublishDateToReadable(
                    SimpleDateFormat(Constants.DateFormat.PROVIDED_DATE_FORMAT, Locale.getDefault()),
                    domainModel.publishedAt),
            urlToImage = domainModel.urlToImage
    )
}