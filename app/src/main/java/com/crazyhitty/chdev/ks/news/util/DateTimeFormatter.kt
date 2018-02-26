package com.crazyhitty.chdev.ks.news.util

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class DateTimeFormatter(private val providedDateFormat: SimpleDateFormat,
                        private val normalizedDateFormat: SimpleDateFormat) {
    fun convertPublishDateToReadable(publishedAt: String): String {
        val publishTime = providedDateFormat.parse(publishedAt).time
        val currentTime = Calendar.getInstance().timeInMillis

        val timeDiff = currentTime - publishTime

        return when {
            timeDiff < TimeUnit.MINUTES.toMillis(1) -> "Now"
            timeDiff < TimeUnit.HOURS.toMillis(1) -> "Today, ${TimeUnit.MILLISECONDS.toMinutes(timeDiff)} mins ago"
            timeDiff < TimeUnit.DAYS.toMillis(1) -> "Today, ${TimeUnit.MILLISECONDS.toHours(timeDiff)} hrs ago"
            timeDiff < TimeUnit.DAYS.toMillis(2) -> "Yesterday, ${TimeUnit.MILLISECONDS.toHours(timeDiff)} hrs ago"
            else -> normalizedDateFormat.format(Date(publishTime))
        }
    }
}