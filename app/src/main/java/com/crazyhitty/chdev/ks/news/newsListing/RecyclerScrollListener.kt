package com.crazyhitty.chdev.ks.news.newsListing

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
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