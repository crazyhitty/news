package com.crazyhitty.chdev.ks.news.util.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Implementation of [SchedulerProvider].
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class AppSchedulerProvider : SchedulerProvider {
    override fun ui(): Scheduler = AndroidSchedulers.mainThread()

    override fun io(): Scheduler = Schedulers.io()
}