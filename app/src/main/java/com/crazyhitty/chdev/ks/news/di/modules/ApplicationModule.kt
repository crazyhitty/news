package com.crazyhitty.chdev.ks.news.di.modules

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module

/**
 * Application level module for providing application specific objects.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
@Module
abstract class ApplicationModule {
    @Binds
    abstract fun provideContext(application: Application): Context
}