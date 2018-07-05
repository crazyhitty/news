package com.crazyhitty.chdev.ks.news.domain

import com.crazyhitty.chdev.ks.news.domain.newslisting.News
import io.reactivex.Single

/**
 * This class caches all of the important data, so that it can be used later on.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
interface NewsRepository {
    fun fetchNews(sources: String, pageSize: Int, page: Int): Single<News>
}