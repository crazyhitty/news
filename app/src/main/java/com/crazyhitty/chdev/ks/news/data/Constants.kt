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

    object NewsListing {
        const val EXTRA_ARTICLES_ITEM = "extra_articles_item"
    }

    object DateFormat {
        const val PROVIDED_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
        const val NORMALIZED_DATE_FORMAT = "h:mm a EEE, MMM d, ''yy"
    }

    object DataStore {
        const val SHARED_PREFERENCES_NAME = "news_data_store"
    }
}