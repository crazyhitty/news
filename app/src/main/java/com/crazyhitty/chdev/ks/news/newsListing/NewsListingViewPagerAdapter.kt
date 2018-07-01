package com.crazyhitty.chdev.ks.news.newsListing

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup

/**
 * This adapter will power the ViewPager being used in the news listing screen.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class NewsListingViewPagerAdapter : PagerAdapter() {
    var sources: Array<String> = emptyArray()

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val newsListingViewGroup = NewsListingViewGroup(container?.context, sources[position])
        container?.addView(newsListingViewGroup)
        return newsListingViewGroup
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        super.destroyItem(container, position, `object`)
        container?.removeView(`object` as NewsListingViewGroup)
    }

    override fun isViewFromObject(view: View?, `object`: Any?) =
            view === `object` as NewsListingViewGroup

    override fun getCount() = sources.size

    override fun getPageTitle(position: Int) = sources[position]
}