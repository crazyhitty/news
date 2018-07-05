package com.crazyhitty.chdev.ks.news.presentation.newslisting

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.crazyhitty.chdev.ks.news.R
import com.crazyhitty.chdev.ks.news.util.extensions.setImageUriOrHide
import com.crazyhitty.chdev.ks.news.util.extensions.setTextOrHide
import com.facebook.drawee.view.SimpleDraweeView
import org.jetbrains.anko.find

/**
 * This recycler adapter is responsible for displaying news.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class NewsRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_TYPE_NEWS = 1
        const val VIEW_TYPE_LOADING = 2
        const val VIEW_TYPE_LOADING_ERROR = 3
    }

    /**
     * Flag indicating what view should be shown for the last item.
     */
    private var lastItemViewType = VIEW_TYPE_LOADING

    /**
     * Error message which will be shown if [LoadingErrorViewHolder] is displayed.
     */
    private var errorMessage: String? = null

    /**
     * List containing articles.
     */
    var articles: MutableList<ArticleViewModel> = ArrayList()
        set(value) {
            // Notify the adapter that new items have been added to it so that it can display
            // these new items.
            val oldSize = itemCount
            field.addAll(value)
            notifyItemRangeInserted(oldSize, itemCount)
        }

    /**
     * Get [ArticleItem] associated with the news item which is clicked
     */
    var onItemClickListener: ((ArticleViewModel) -> Unit)? = null

    /**
     * Called when error view is clicked by the user.
     */
    var onErrorViewClickListener: (() -> Unit)? = null

    /**
     * Show [LoadingErrorViewHolder] for last item in the recycler view instead of
     * [LoadingViewHolder].
     *
     * @param message   Error message to be displayed.
     */
    fun showErrorView(message: String) {
        errorMessage = message
        lastItemViewType = VIEW_TYPE_LOADING_ERROR
        notifyItemChanged(itemCount.minus(1))
    }

    /**
     * Show [LoadingViewHolder] for last item in recycler view instead of [LoadingErrorViewHolder].
     */
    fun showLoadingView() {
        lastItemViewType = VIEW_TYPE_LOADING
        notifyItemChanged(itemCount.minus(1))
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_NEWS -> {
                val view = LayoutInflater.from(parent?.context)
                        .inflate(R.layout.item_news, parent, false)
                NewsViewHolder(view)
            }
            VIEW_TYPE_LOADING -> {
                val view = LayoutInflater.from(parent?.context)
                        .inflate(R.layout.item_loading, parent, false)
                LoadingViewHolder(view)
            }
            VIEW_TYPE_LOADING_ERROR -> {
                val view = LayoutInflater.from(parent?.context)
                        .inflate(R.layout.item_loading_error, parent, false)
                LoadingErrorViewHolder(view)
            }
            else -> throw IllegalArgumentException("This viewType($viewType) is not supported")
        }
    }

    /**
     * Provides the count of total items to be shown in recycler view.
     *
     * @return
     * Size of news articles + 1(loading view or loading error view)
     */
    override fun getItemCount() = when {
        articles.size == 0 -> 0
        else -> articles.size.plus(1)
    }

    /**
     * Provides the view type for the item for this position.
     *
     * @param position  Position of the adapter item
     *
     * @return
     * View type for this item.
     */
    override fun getItemViewType(position: Int) = when (position) {
        itemCount.minus(1) -> lastItemViewType
        else -> VIEW_TYPE_NEWS
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (holder) {
            is NewsViewHolder -> onBindNewsViewHolder(holder, position)
            is LoadingViewHolder -> onBindLoadingViewHolder(holder, position)
            is LoadingErrorViewHolder -> onBindLoadingErrorViewHolder(holder, position)
        }
    }

    private fun onBindNewsViewHolder(holder: NewsViewHolder?, position: Int) {
        val article = articles[position]

        holder?.textViewTitle?.text = article.title
        holder?.textViewDesc?.setTextOrHide(article.description)
        holder?.textViewAuthor?.text = article.author
        holder?.textViewDate?.text = article.publishedAt
        holder?.imageViewNews?.setImageUriOrHide(false, article.urlToImage)
    }

    private fun onBindLoadingViewHolder(holder: LoadingViewHolder?, position: Int) {

    }

    private fun onBindLoadingErrorViewHolder(holder: LoadingErrorViewHolder?, position: Int) {
        holder?.textViewError?.text = errorMessage.plus("\nTap to retry")
    }

    inner class NewsViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle = itemView?.find<TextView>(R.id.textViewTitle)
        val textViewDesc = itemView?.find<TextView>(R.id.textViewDesc)
        val textViewAuthor = itemView?.find<TextView>(R.id.textViewAuthor)
        val textViewDate = itemView?.find<TextView>(R.id.textViewDate)
        val imageViewNews = itemView?.find<SimpleDraweeView>(R.id.imageViewNews)

        init {
            itemView?.setOnClickListener {
                onItemClickListener?.invoke(articles[adapterPosition])
            }
        }
    }

    inner class LoadingViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val progressBar = itemView?.find<ProgressBar>(R.id.progressBar)
    }

    inner class LoadingErrorViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val textViewError = itemView?.find<TextView>(R.id.textViewError)

        init {
            itemView?.setOnClickListener {
                onErrorViewClickListener?.invoke()
            }
        }
    }
}