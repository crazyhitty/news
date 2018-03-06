package com.crazyhitty.chdev.ks.news.sources

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.crazyhitty.chdev.ks.news.R
import com.crazyhitty.chdev.ks.news.data.api.model.news.SourceItem
import org.jetbrains.anko.find

/**
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class SourcesRecyclerAdapter : RecyclerView.Adapter<SourcesRecyclerAdapter.SourceViewHolder>() {
    var sourceItems: ArrayList<SourceItem?> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun clear() {
        sourceItems.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SourceViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_source, parent, false)
        return SourceViewHolder(view)
    }

    override fun onBindViewHolder(holder: SourceViewHolder?, position: Int) {
        val sourceItem = sourceItems[position]

        holder?.textViewName?.text = sourceItem?.name
        holder?.textViewDesc?.text = sourceItem?.description
        holder?.textViewExtraDetails?.text = "${sourceItem?.category} / ${sourceItem?.language} / ${sourceItem?.country}"
    }

    override fun getItemCount() = sourceItems.size

    inner class SourceViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val textViewName = itemView?.find<TextView>(R.id.textViewName)
        val textViewDesc = itemView?.find<TextView>(R.id.textViewDesc)
        val textViewExtraDetails = itemView?.find<TextView>(R.id.textViewExtraDetails)
    }
}