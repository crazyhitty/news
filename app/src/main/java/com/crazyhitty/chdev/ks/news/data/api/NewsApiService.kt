package com.crazyhitty.chdev.ks.news.data.api

import com.crazyhitty.chdev.ks.news.data.api.model.news.News
import com.crazyhitty.chdev.ks.news.data.api.model.news.Source
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Contains all of the API endpoints for news.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
interface NewsApiService {
    /**
     * Fetches all of the news sources available.
     *
     * @return
     * [Single] observable containing [Source].
     */
    @GET("/v2/sources")
    fun sources(): Single<Source>

    /**
     * Fetches the top headlines.
     *
     * @return
     * [Single] observable containing [News].
     */
    @GET("/v2/top-headlines")
    fun topHeadlines(@Query("country") country: String,
                     @Query("pageNumber") pageNumber: Int): Single<News>

    /**
     * Fetches all of the news from a particular source.
     *
     * @return
     * [Single] observable containing [News].
     */
    @GET("/v2/everything")
    fun everything(@Query("source") source: String): Single<News>
}