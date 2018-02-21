package com.crazyhitty.chdev.ks.news.di.components

import com.crazyhitty.chdev.ks.news.NewsApplication
import com.crazyhitty.chdev.ks.news.data.api.NewsApiService
import com.crazyhitty.chdev.ks.news.di.modules.ApplicationModule
import com.crazyhitty.chdev.ks.news.util.internet.InternetHelper
import com.crazyhitty.chdev.ks.news.util.rx.SchedulerProvider
import dagger.Component
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

/**
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    fun inject(newsApplication: NewsApplication)

    fun getCompositeDisposable(): CompositeDisposable

    fun getSchedulerProvider(): SchedulerProvider

    fun getInternetHelper(): InternetHelper

    fun getNewsApiService(): NewsApiService
}