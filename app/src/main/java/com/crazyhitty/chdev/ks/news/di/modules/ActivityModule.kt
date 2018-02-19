package com.crazyhitty.chdev.ks.news.di.modules

import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.crazyhitty.chdev.ks.news.di.ActivityContext
import com.crazyhitty.chdev.ks.news.newsListing.NewsListingContract
import com.crazyhitty.chdev.ks.news.newsListing.NewsListingPresenter
import com.crazyhitty.chdev.ks.news.newsListing.NewsRecyclerAdapter
import dagger.Module
import dagger.Provides

/**
 * Activity level module for providing activity specific objects.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
@Module
class ActivityModule(private val appCompatActivity: AppCompatActivity) {
    @Provides
    @ActivityContext
    fun provideContext(): Context {
        return appCompatActivity
    }

    @Provides
    fun provideActivity(): Activity {
        return appCompatActivity
    }

    @Provides
    fun provideNewsPresenter(newsListingPresenter: NewsListingPresenter):
            NewsListingContract.Presenter {
        return newsListingPresenter
    }

    @Provides
    fun provideNewsRecyclerAdapter() = NewsRecyclerAdapter()

    @Provides
    fun provideLinearLayoutManager(@ActivityContext context: Context): LinearLayoutManager =
            LinearLayoutManager(context)
}