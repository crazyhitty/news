package com.crazyhitty.chdev.ks.news.data

import com.crazyhitty.chdev.ks.news.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Responsible for providing access to News API.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class NewsApi {
    companion object {
        private const val BASE_URL = "https://newsapi.org"
        private const val HEADER_X_API_KEY = "X-Api-Key"
        private const val API_KEY = BuildConfig.API_KEY
        private val OK_HTTP_CLIENT = OkHttpClient.Builder()
                .addInterceptor {
                    it.proceed(it.request()
                            .newBuilder()
                            .addHeader(HEADER_X_API_KEY, API_KEY)
                            .build())
                }
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

        /**
         * Provides access to [NewsApiService].
         *
         * @return
         * Instance of [NewsApiService].
         */
        fun getApiService(): NewsApiService {
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(MoshiConverterFactory.create())
                    .client(OK_HTTP_CLIENT)
                    .build()
                    .create(NewsApiService::class.java)
        }
    }
}