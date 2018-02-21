package com.crazyhitty.chdev.ks.news.newsListing

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.crazyhitty.chdev.ks.news.R
import com.crazyhitty.chdev.ks.news.data.api.model.news.ArticlesItem
import com.crazyhitty.chdev.ks.news.data.api.model.news.News
import com.crazyhitty.chdev.ks.news.util.extensions.setImageUriOrHide
import com.crazyhitty.chdev.ks.news.util.extensions.setTextOrHide
import com.facebook.drawee.view.SimpleDraweeView
import org.jetbrains.anko.find

/**
 * This recycler adapter is responsible for displaying news.
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class NewsRecyclerAdapter : RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder>() {
    var news: News? = null
    set(value) {
        field = value
        notifyDataSetChanged()
    }


    fun clear() {
        news = null
        notifyDataSetChanged()
    }

    var onItemClickListener: ((ArticlesItem?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return news?.totalResults ?: 0
    }

    override fun onBindViewHolder(holder: NewsViewHolder?, position: Int) {
        val article = news?.articles?.get(position)

        holder?.textViewTitle?.text = article?.title
        holder?.textViewDesc?.setTextOrHide(article?.description)
        holder?.textViewSource?.text = article?.source?.name
        holder?.textViewDate?.text = article?.publishedAt
        holder?.imageViewNews?.setImageUriOrHide(article?.urlToImage)
    }

    inner class NewsViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle = itemView?.find<TextView>(R.id.textViewTitle)
        val textViewDesc = itemView?.find<TextView>(R.id.textViewDesc)
        val textViewSource = itemView?.find<TextView>(R.id.textViewSource)
        val textViewDate = itemView?.find<TextView>(R.id.textViewDate)
        val imageViewNews = itemView?.find<SimpleDraweeView>(R.id.imageViewNews)
        init {
            itemView?.setOnClickListener {
                onItemClickListener?.invoke(news?.articles?.get(adapterPosition))
            }
        }
    }
}