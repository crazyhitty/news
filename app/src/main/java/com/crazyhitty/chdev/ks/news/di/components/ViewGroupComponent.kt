package com.crazyhitty.chdev.ks.news.di.components

import com.crazyhitty.chdev.ks.news.di.modules.ViewGroupModule
import com.crazyhitty.chdev.ks.news.di.scopes.PerViewGroup
import com.crazyhitty.chdev.ks.news.newsListing.NewsListingViewGroup
import dagger.Component

/**
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
@PerViewGroup
@Component(modules = [ViewGroupModule::class])
interface ViewGroupComponent {
    /**
     * Inject in [NewsListingViewGroup].
     */
    fun inject(newsListingViewGroup: NewsListingViewGroup)
}