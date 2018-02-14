package com.crazyhitty.chdev.ks.news.news

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.crazyhitty.chdev.ks.news.R
import com.crazyhitty.chdev.ks.news.data.model.news.News
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

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return news?.totalResults ?: 0
    }

    override fun onBindViewHolder(holder: NewsViewHolder?, position: Int) {
        holder?.textViewTitle?.text = news?.articles?.get(position)?.title
    }


    inner class NewsViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle = itemView?.find<TextView>(R.id.text_view_title)
    }
}