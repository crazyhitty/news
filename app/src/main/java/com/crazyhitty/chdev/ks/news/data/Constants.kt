package com.crazyhitty.chdev.ks.news.data

import com.crazyhitty.chdev.ks.news.BuildConfig

/**
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
object Constants {
    object Api {
        const val BASE_URL = "https://newsapi.org"
        const val HEADER_X_API_KEY = "X-Api-Key"
        const val API_KEY = BuildConfig.API_KEY
    }
}