package com.crazyhitty.chdev.ks.news.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.crazyhitty.chdev.ks.news.Constants
import dagger.Module
import dagger.Provides

@Module
class DataModule {
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences =
            context.getSharedPreferences(Constants.DataStore.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
}