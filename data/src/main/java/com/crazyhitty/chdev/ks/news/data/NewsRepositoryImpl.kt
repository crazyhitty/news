package com.crazyhitty.chdev.ks.news.data

import com.crazyhitty.chdev.ks.news.data.newslisting.NewsMapper
import com.crazyhitty.chdev.ks.news.domain.newslisting.News
import com.crazyhitty.chdev.ks.news.domain.NewsRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * Implementation of [NewsRepository]. This uses SharedPreferences internally for the saving and
 * fetching of data.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class NewsRepositoryImpl @Inject constructor(
        private val api: NewsApi,
        private val mapper: NewsMapper
) : NewsRepository {
    override fun fetchNews(sources: String, pageSize: Int, page: Int): Single<News> =
            api.everything(sources, pageSize, page).map { mapper.transform(it) }
}