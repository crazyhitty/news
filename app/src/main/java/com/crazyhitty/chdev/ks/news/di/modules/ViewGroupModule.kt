package com.crazyhitty.chdev.ks.news.di.modules

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.ViewGroup
import com.crazyhitty.chdev.ks.news.di.ViewGroupContext
import com.crazyhitty.chdev.ks.news.newsListing.NewsRecyclerAdapter
import dagger.Module
import dagger.Provides

/**
 * ViewGroup level module for providing view group specific objects. Don't confuse it with MVP's
 * view, this module is just providing dependency injection for custom view group implementations.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
@Module
class ViewGroupModule(private val viewGroup: ViewGroup) {
    /**
     * Provide context specific to the [ViewGroup].
     */
    @Provides
    @ViewGroupContext
    fun provideContext(): Context = viewGroup.context

    /**
     * Provide the [ViewGroup] itself.
     */
    @Provides
    fun provideViewGroup() = viewGroup

    /**
     * Provide [NewsRecyclerAdapter] for the [ViewGroup].
     */
    @Provides
    fun provideNewsRecyclerAdapter() = NewsRecyclerAdapter()

    /**
     * Provide [LinearLayoutManager] for the [ViewGroup].
     */
    @Provides
    fun provideLinearLayoutManager(@ViewGroupContext context: Context): LinearLayoutManager =
            LinearLayoutManager(context)
}