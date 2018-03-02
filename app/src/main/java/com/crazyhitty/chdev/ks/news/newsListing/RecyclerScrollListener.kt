package com.crazyhitty.chdev.ks.news.newsListing

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * Type of [RecyclerView.OnScrollListener] which checks if a particular item was shown on the
 * screen or not.
 *
 * @param linearLayoutManager   [LinearLayoutManager] used with the recycler view
 * @param positionFromLast      Position to be checked for appearance, must be counted from last.
 *                              Example: If you are checking for last 6th item's appearance provide
 *                              6
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
open class RecyclerScrollListener(private val linearLayoutManager: LinearLayoutManager,
                                  private val positionFromLast: Int) : RecyclerView.OnScrollListener() {
    private val positionsAppearedCache = HashSet<Int>()

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        recyclerView?.adapter?.itemCount?.let {
            checkPositionAppearance(it)
        }
    }

    /**
     * Checks if the provided position was appeared or not in the recycler view.
     *
     * @param itemCount Number of items available in recycler view adapter.
     */
    private fun checkPositionAppearance(itemCount: Int) {
        val lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition()
        val positionToBeChecked = itemCount.minus(positionFromLast)
        if (!positionsAppearedCache.contains(positionToBeChecked) &&
                lastVisibleItemPosition == positionToBeChecked) {
            onPositionAppeared(positionToBeChecked)
        }
    }

    /**
     * Called when the recycler item with provided position is visible on the screen. Must call
     * super method also if you want to cache this position so this method won't be called again
     * for the same position.
     *
     * @param position  Current position of the recycler view item which got appeared
     */
    open fun onPositionAppeared(position: Int) {
        positionsAppearedCache.add(position)
    }
}