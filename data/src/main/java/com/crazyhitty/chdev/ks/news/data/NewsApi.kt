package com.crazyhitty.chdev.ks.news.data

import com.crazyhitty.chdev.ks.news.data.newslisting.NewsDto
import com.crazyhitty.chdev.ks.news.data.newslisting.SourceDto
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Contains all of the API endpoints for news.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
interface NewsApi {
    /**
     * Fetches all of the news sources available.
     *
     * @return
     * [Single] observable containing [SourceDto].
     */
    @GET("/v2/sources")
    fun sources(): Single<SourceDto>

    /**
     * Fetches all of the news from a particular sourceItem.
     *
     * @param sources   Provide sourceItem from which news should be fetched.
     * @param pageSize  Number of articles that should be provided in a single request.
     * @param page      Current page number for maintaining pagination.
     *
     * @return
     * [Single] observable containing [NewsDto].
     */
    @GET("/v2/everything")
    fun everything(@Query("sources") sources: String,
                   @Query("pageSize") pageSize: Int,
                   @Query("page") page: Int): Single<NewsDto>
}