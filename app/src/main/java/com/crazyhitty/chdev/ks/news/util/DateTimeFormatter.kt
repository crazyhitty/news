package com.crazyhitty.chdev.ks.news.util

import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class DateTimeFormatter(private val providedDateFormat: DateFormat) {
    fun convertPublishDateToReadable(publishedAt: String): String {
        val publishTime = providedDateFormat.parse(publishedAt).time
        val currentTime = Calendar.getInstance().timeInMillis

        val timeDiff = currentTime - publishTime

        return when {
            timeDiff < TimeUnit.MINUTES.toMillis(1) -> "Now"
            timeDiff < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(timeDiff)} mins ago"
            timeDiff < TimeUnit.DAYS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toHours(timeDiff)} hrs ago"
            timeDiff < TimeUnit.DAYS.toMillis(2) -> "Yesterday"
            else -> "${TimeUnit.MILLISECONDS.toDays(timeDiff)} days ago"
        }
    }
}