package com.crazyhitty.chdev.ks.news.presentation.newslisting

import dagger.Binds
import dagger.Module

@Module
abstract class NewsListingViewModule {
    @Binds
    internal abstract fun provideNewsListingView(newsListingActivity: NewsListingActivity): NewsListingContract.View
}