package com.crazyhitty.chdev.ks.news.data

import com.crazyhitty.chdev.ks.news.data.api.model.news.Sources

/**
 * This class caches all of the important data, so that it can be used later on.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
interface DataStore {
    /**
     * Save the sources.
     *
     * @param sources   [Sources] to be saved
     */
    fun saveSources(sources: Sources)

    /**
     * Get the available sources.
     *
     * @return
     * [Sources] if available, otherwise null.
     */
    fun getSources(): Sources?

    /**
     * Should be called if user has selected the sources when the application was started for the
     * first time.
     *
     * @param status    True, if selection was completed, false otherwise
     */
    fun sourcesSelectionComplete(status: Boolean)
}