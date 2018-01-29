package com.crazyhitty.chdev.ks.news.util.rx

import io.reactivex.Scheduler

/**
 * Provide appropriate rx java scheduler.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
interface SchedulerProvider {
    /**
     * Provide scheduler that will post events on UI thread.
     */
    fun ui(): Scheduler

    /**
     * Provide scheduler that will post events on IO bound threads.
     */
    fun io(): Scheduler
}