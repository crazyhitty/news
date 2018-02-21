package com.crazyhitty.chdev.ks.news.data.api

/**
 * Data class which will contain information about News API.
 *
 * @param baseUrl   Base url endpoint for this API
 * @param apiHeader Header param key for API authentication
 * @param apiKey    Valid key for communicating with this API
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
data class NewsApiConfig(val baseUrl: String, val apiHeader: String, val apiKey: String)