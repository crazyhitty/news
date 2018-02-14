package com.crazyhitty.chdev.ks.news.util.internet

import android.net.ConnectivityManager

/**
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class AppInternetHelper(private val connectivityManager: ConnectivityManager) : InternetHelper {
    override fun isAvailable(): Boolean {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null &&
                activeNetworkInfo.isConnectedOrConnecting
    }
}