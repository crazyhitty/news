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
     * Fetches all of the news from a particular source.
     *
     * @param sources   Provide source from which news should be fetched.
     * @param pageSize  Number of articles that should be provided in a single request.
     * @param page      Current page number for maintaining pagination.
     *
     * @return
     * [Single] observable containing [News].
     */
    @GET("/v2/everything")
    fun everything(@Query("sources") sources: String,
                   @Query("pageSize") pageSize: Int,
                   @Query("page") page: Int): Single<News>
}