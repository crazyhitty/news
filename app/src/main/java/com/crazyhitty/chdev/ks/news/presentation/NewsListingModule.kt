package com.crazyhitty.chdev.ks.news.presentation

import com.crazyhitty.chdev.ks.news.data.NewsApi
import com.crazyhitty.chdev.ks.news.data.NewsRepositoryImpl
import com.crazyhitty.chdev.ks.news.domain.NewsRepository
import com.crazyhitty.chdev.ks.news.presentation.newslisting.NewsListingActivity
import com.crazyhitty.chdev.ks.news.presentation.newslisting.NewsListingViewModule
import com.crazyhitty.chdev.ks.news.presentation.splash.SplashActivity
import com.crazyhitty.chdev.ks.news.presentation.splash.SplashViewModule
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NewsListingModule.Declarations::class])
abstract class NewsListingModule {
    @Module
    class Declarations {
        @Provides
        @Singleton
        fun provideNewsApi(retrofit: Retrofit): NewsApi = retrofit.create(NewsApi::class.java)
    }

    @Singleton
    @Binds
    abstract fun provideNewsRepository(newsRepositoryImpl: NewsRepositoryImpl): NewsRepository

    @ContributesAndroidInjector(
            modules = [NewsListingViewModule::class]
    )
    abstract fun bindNewsListingActivity(): NewsListingActivity

    @ContributesAndroidInjector(
            modules = [SplashViewModule::class]
    )
    abstract fun bindSplashActivity(): SplashActivity
}