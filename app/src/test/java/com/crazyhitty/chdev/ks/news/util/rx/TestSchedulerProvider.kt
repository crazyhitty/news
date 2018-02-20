package com.crazyhitty.chdev.ks.news.util.rx

import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler

/**
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class TestSchedulerProvider(private val testScheduler: TestScheduler) : SchedulerProvider {

    override fun ui(): Scheduler = testScheduler

    override fun io(): Scheduler = testScheduler
}