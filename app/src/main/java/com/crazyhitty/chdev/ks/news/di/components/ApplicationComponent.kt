package com.crazyhitty.chdev.ks.news.di.components

import android.app.Application
import com.crazyhitty.chdev.ks.news.NewsApplication
import com.crazyhitty.chdev.ks.news.di.modules.ApplicationModule
import com.crazyhitty.chdev.ks.news.di.modules.DataModule
import com.crazyhitty.chdev.ks.news.di.modules.NetworkModule
import com.crazyhitty.chdev.ks.news.presentation.NewsListingModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    ApplicationModule::class,
    NetworkModule::class,
    DataModule::class,
    NewsListingModule::class
])
interface ApplicationComponent : AndroidInjector<NewsApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }
}