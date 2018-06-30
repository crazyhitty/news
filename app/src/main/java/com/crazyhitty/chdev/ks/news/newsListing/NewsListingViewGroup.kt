package com.crazyhitty.chdev.ks.news.newsListing

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.crazyhitty.chdev.ks.news.R
import com.crazyhitty.chdev.ks.news.data.api.model.news.ArticleItem
import com.crazyhitty.chdev.ks.news.di.components.DaggerViewGroupComponent
import com.crazyhitty.chdev.ks.news.di.components.ViewGroupComponent
import com.crazyhitty.chdev.ks.news.di.modules.ViewGroupModule
import org.jetbrains.anko.find
import javax.inject.Inject

/**
 * This view is responsible for rendering list of news associated with a particular source.
 *
 * @param context   Current activity context.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
@SuppressLint("ViewConstructor")
class NewsListingViewGroup : RelativeLayout {
    private val viewGroupComponent: ViewGroupComponent by lazy {
        DaggerViewGroupComponent.builder()
                .viewGroupModule(ViewGroupModule(this))
                .build()
    }

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager
    @Inject
    lateinit var newsRecyclerAdapter: NewsRecyclerAdapter

    private lateinit var recyclerViewNews: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var textViewNewsUnavailable: TextView
    private lateinit var progressBar: ProgressBar

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    init {
        // Inject view group component.
        viewGroupComponent.inject(this)

        // Inflate layout.
        val layoutInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutInflater.inflate(R.layout.view_news_listing, this)

        // Set layout params for this FrameLayout.
        val layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        setLayoutParams(layoutParams)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        // Get view references.
        recyclerViewNews = find(R.id.recyclerViewNews)
        swipeRefreshLayout = find(R.id.swipeRefreshLayout)
        textViewNewsUnavailable = find(R.id.textViewNewsUnavailable)
        progressBar = find(R.id.progressBar)

        recyclerViewNews.layoutManager = linearLayoutManager
        recyclerViewNews.adapter = newsRecyclerAdapter
    }

    /**
     * Listen for news item click events.
     *
     * @param onClick   Function containing [ArticleItem]
     */
    fun onNewsItemClick(onClick: (articleItem: ArticleItem?) -> Unit) {
        newsRecyclerAdapter.onItemClickListener = { onClick(it) }
    }

    /**
     * Listen for news error view click events.
     *
     * @param onClick   Function called when news error view is clicked.
     */
    fun onNewsErrorViewClick(onClick: () -> Unit) {
        newsRecyclerAdapter.onErrorViewClickListener = { onClick() }
    }

    /**
     * Listen when swipe to refresh is called.
     *
     * @param onRefresh   Function called when swipe down to refresh happens
     */
    fun onSwipeDownRefresh(onRefresh: () -> Unit) {
        swipeRefreshLayout.setOnRefreshListener {
            onRefresh()
        }
    }

    /**
     * Enable [SwipeRefreshLayout].
     */
    fun enableSwipeDownToRefresh() {
        swipeRefreshLayout.isEnabled = true
    }

    /**
     * Disable [SwipeRefreshLayout].
     */
    fun disableSwipeDownToRefresh() {
        swipeRefreshLayout.isEnabled = false
    }

    /**
     * Show [ProgressBar].
     */
    fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    /**
     * Hide [ProgressBar].
     */
    fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    /**
     * Update news listing.
     *
     * @param articles  [ArrayList] containing [ArticleItem]s
     */
    fun updateNewsListing(articles: ArrayList<ArticleItem?>) {
        newsRecyclerAdapter.articles = articles
    }

    /**
     * Remove all the news items from the UI.
     */
    fun clearNewsListing() {
        newsRecyclerAdapter.clear()
    }

    /**
     * Stop refreshing [SwipeRefreshLayout].
     */
    fun stopSwipeDownToRefresh() {
        swipeRefreshLayout.isRefreshing = false
    }

    /**
     * Hide the error text.
     */
    fun hideErrorText() {
        textViewNewsUnavailable.visibility = View.GONE
    }

    /**
     * Show the error text.
     */
    fun showErrorText() {
        textViewNewsUnavailable.visibility = View.GONE
    }

    /**
     * Update the error text.
     *
     * @param text  [String] message indicating error
     */
    fun updateErrorText(text: String) {
        textViewNewsUnavailable.text = text
    }

    /**
     * Show error view on the last item in news listing.
     */
    fun showRecyclerLoadMoreErrorView(message: String) {
        newsRecyclerAdapter.showErrorView(message)
    }

    /**
     * Show loading view on the last item in news listing.
     */
    fun showRecyclerLoadingView() {
        newsRecyclerAdapter.showLoadingView()
    }

    /**
     * Listen when a particular news item with the provided position is appeared for the first
     * time on the screen.
     *
     * @param positionFromLast  Position to be checked for appearance, must be counted from last.
     */
    fun onNewsScrollItemAppeared(positionFromLast: Int, onItemAppearance: (position: Int) -> Unit) {
        recyclerViewNews.addOnScrollListener(object : RecyclerScrollListener(linearLayoutManager, positionFromLast) {
            override fun onPositionAppeared(position: Int) {
                super.onPositionAppeared(position)
                onItemAppearance(position)
            }
        })
    }

    /**
     * Stop listening for news item appearance.
     */
    fun stopNewsScrollListener() {
        recyclerViewNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {})
    }
}