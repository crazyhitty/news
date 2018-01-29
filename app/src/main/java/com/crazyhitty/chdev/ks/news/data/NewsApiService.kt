package com.crazyhitty.chdev.ks.news.data

import com.crazyhitty.chdev.ks.news.data.model.news.News
import io.reactivex.Single
import retrofit2.http.GET

/**
 * Contains all of the API endpoints for news.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
interface NewsApiService {

    /**
     * Fetches the top headlines.
     *
     * @return
     * [Single] observable containing [News].
     */
    @GET("/v2/top-headlines")
    fun topHeadlines(): Single<News>
}