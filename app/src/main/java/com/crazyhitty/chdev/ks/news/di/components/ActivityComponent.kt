package com.crazyhitty.chdev.ks.news.di.components

import com.crazyhitty.chdev.ks.news.di.modules.ActivityModule
import com.crazyhitty.chdev.ks.news.di.scopes.PerActivity
import com.crazyhitty.chdev.ks.news.newsListing.NewsListingActivity
import com.crazyhitty.chdev.ks.news.sources.SourcesActivity
import com.crazyhitty.chdev.ks.news.splash.SplashActivity
import dagger.Component

/**
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
@PerActivity
@Component(dependencies = [ApplicationComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(newsListingActivity: NewsListingActivity)

    fun inject(sourcesActivity: SourcesActivity)

    fun inject(splashActivity: SplashActivity)
}