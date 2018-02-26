package com.crazyhitty.chdev.ks.news.di.modules

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.crazyhitty.chdev.ks.news.R
import com.crazyhitty.chdev.ks.news.data.Constants
import com.crazyhitty.chdev.ks.news.data.api.NewsApiConfig
import com.crazyhitty.chdev.ks.news.data.api.NewsApiService
import com.crazyhitty.chdev.ks.news.di.ApiConfig
import com.crazyhitty.chdev.ks.news.di.ApplicationContext
import com.crazyhitty.chdev.ks.news.di.NormalizedDate
import com.crazyhitty.chdev.ks.news.di.ProvidedDate
import com.crazyhitty.chdev.ks.news.util.DateTimeFormatter
import com.crazyhitty.chdev.ks.news.util.internet.AppInternetHelper
import com.crazyhitty.chdev.ks.news.util.internet.InternetHelper
import com.crazyhitty.chdev.ks.news.util.rx.AppSchedulerProvider
import com.crazyhitty.chdev.ks.news.util.rx.SchedulerProvider
import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


/**
 * Application level module for providing application specific objects.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
@Module
class ApplicationModule(private val application: Application) {
    @Provides
    @ApplicationContext
    fun provideContext(): Context = application

    @Provides
    fun provideApplication() = application

    @Provides
    @Singleton
    fun provideCalligraphyConfig(): CalligraphyConfig = CalligraphyConfig.Builder()
            .setDefaultFontPath("fonts/WorkSans-Regular.ttf")
            .setFontAttrId(R.attr.fontPath)
            .build()

    @Provides
    fun provideCompositeDisposable() = CompositeDisposable()

    @Provides
    fun provideSchedulerProvider(): SchedulerProvider = AppSchedulerProvider()

    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context) =
            (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)

    @Provides
    @Singleton
    fun provideInternetHelper(connectivityManager: ConnectivityManager): InternetHelper =
            AppInternetHelper(connectivityManager)

    @Provides
    @ApiConfig
    fun provideApiConfig() = NewsApiConfig(Constants.Api.BASE_URL,
            Constants.Api.HEADER_X_API_KEY,
            Constants.Api.API_KEY)

    @Provides
    @Singleton
    fun provideNetworkInterceptor(): Interceptor = StethoInterceptor()

    @Provides
    @Singleton
    fun provideCallAdapterFactory(): CallAdapter.Factory = RxJava2CallAdapterFactory.create()

    @Provides
    @Singleton
    fun provideConverterFactory(): Converter.Factory = MoshiConverterFactory.create()

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApiConfig apiConfig: NewsApiConfig,
                            networkInterceptor: Interceptor): OkHttpClient =
            OkHttpClient.Builder()
                    .addInterceptor {
                        it.proceed(it.request()
                                .newBuilder()
                                .addHeader(apiConfig.apiHeader, apiConfig.apiKey)
                                .build())
                    }
                    .addNetworkInterceptor(networkInterceptor)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build()

    @Provides
    @Singleton
    fun provideRetrofit(@ApiConfig apiConfig: NewsApiConfig,
                        callAdapterFactory: CallAdapter.Factory,
                        converterFactory: Converter.Factory,
                        okHttpClient: OkHttpClient): NewsApiService =
            Retrofit.Builder()
                    .baseUrl(apiConfig.baseUrl)
                    .addCallAdapterFactory(callAdapterFactory)
                    .addConverterFactory(converterFactory)
                    .client(okHttpClient)
                    .build()
                    .create(NewsApiService::class.java)

    @Provides
    @ProvidedDate
    fun provideProvidedDate() = SimpleDateFormat(Constants.DateFormat.PROVIDED_DATE_FORMAT,
            Locale.getDefault())

    @Provides
    @NormalizedDate
    fun provideNormalizedDate() = SimpleDateFormat(Constants.DateFormat.NORMALIZED_DATE_FORMAT,
            Locale.getDefault())

    @Provides
    @Singleton
    fun provideDateTimeFormatter(@ProvidedDate providedDateFormat: SimpleDateFormat,
                                 @NormalizedDate normalizedDateFormat: SimpleDateFormat) =
            DateTimeFormatter(providedDateFormat, normalizedDateFormat)
}