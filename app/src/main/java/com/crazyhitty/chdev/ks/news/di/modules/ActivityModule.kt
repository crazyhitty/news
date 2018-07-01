package com.crazyhitty.chdev.ks.news.di.modules

import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.crazyhitty.chdev.ks.news.di.ActivityContext
import com.crazyhitty.chdev.ks.news.sources.SourcesContract
import com.crazyhitty.chdev.ks.news.sources.SourcesPresenter
import com.crazyhitty.chdev.ks.news.sources.SourcesRecyclerAdapter
import com.crazyhitty.chdev.ks.news.splash.SplashContract
import com.crazyhitty.chdev.ks.news.splash.SplashPresenter
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
    fun provideContext(): Context = appCompatActivity

    @Provides
    fun provideActivity(): Activity = appCompatActivity

    @Provides
    fun provideSourcesPresenter(sourcesPresenter: SourcesPresenter): SourcesContract.Presenter =
            sourcesPresenter

    @Provides
    fun provideSplashPresenter(splashPresenter: SplashPresenter): SplashContract.Presenter =
            splashPresenter

    @Provides
    fun provideSourcesRecyclerAdapter() = SourcesRecyclerAdapter()

    @Provides
    fun provideLinearLayoutManager(@ActivityContext context: Context): LinearLayoutManager =
            LinearLayoutManager(context)
}