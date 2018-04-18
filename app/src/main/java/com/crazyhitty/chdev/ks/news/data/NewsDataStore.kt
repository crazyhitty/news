package com.crazyhitty.chdev.ks.news.data

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.crazyhitty.chdev.ks.news.data.api.model.news.Sources
import com.squareup.moshi.Moshi

/**
 * Implementation of [DataStore]. This uses SharedPreferences internally for the saving and
 * fetching of data.
 *
 * @param
 * @param sharedPreferences   Instance of [SharedPreferences] maintained by this application
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class NewsDataStore(private val moshi: Moshi, private val sharedPreferences: SharedPreferences) : DataStore {
    companion object {
        private const val SOURCES = "sources"
        private const val SOURCE_SELECTION_COMPLETED = "source_selection_completed"
    }

    /**
     * Save the sources in shared preferences. This method is not asynchronous and might lag the UI
     * if called via main thread.
     *
     * @param sources   [Sources] to be saved
     */
    @SuppressLint("ApplySharedPref")
    override fun saveSources(sources: Sources) {
        val adapter = moshi.adapter(Sources::class.java)
        val sourcesJson = adapter.toJson(sources)
        sharedPreferences.edit()
                .putString(SOURCES, sourcesJson)
                .commit()
    }

    /**
     * Get the available sources from shared preferences.
     *
     * @return
     * [Sources] fetched from the shared preferences. Might return null if no [Sources] are
     * available in shared preferences.
     */
    override fun getSources(): Sources? {
        val sourcesJson = sharedPreferences.getString(SOURCES, null) ?: return null
        val adapter = moshi.adapter(Sources::class.java)
        return adapter.fromJson(sourcesJson)
    }

    /**
     * Should be called if user has selected the sources when the application was started for the
     * first time. This method is not asynchronous and might lag the UI if called via main thread.
     *
     * @param status    True, if selection was completed, false otherwise
     */
    @SuppressLint("ApplySharedPref")
    override fun sourcesSelectionComplete(status: Boolean) {
        sharedPreferences.edit()
                .putBoolean(SOURCE_SELECTION_COMPLETED, status)
                .commit()
    }
}