package com.crazyhitty.chdev.ks.news.util.internet

/**
 * Provides internet related utility methods.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
interface InternetHelper {
    /**
     * Check if internet is available or not.
     *
     * @return
     * True, if internet is available, otherwise false.
     */
    fun isAvailable(): Boolean
}