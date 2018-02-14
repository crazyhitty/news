package com.crazyhitty.chdev.ks.news.di.components

import com.crazyhitty.chdev.ks.news.di.scopes.PerActivity
import com.crazyhitty.chdev.ks.news.di.modules.ActivityModule
import com.crazyhitty.chdev.ks.news.news.NewsActivity
import dagger.Component

/**
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
@PerActivity
@Component(dependencies = [ApplicationComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(newsActivity: NewsActivity)
}