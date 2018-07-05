package com.crazyhitty.chdev.ks.news.presentation.splash

import dagger.Binds
import dagger.Module

@Module
abstract class SplashViewModule {
    @Binds
    internal abstract fun provideSplashView(splashActivity: SplashActivity): SplashContract.View
}