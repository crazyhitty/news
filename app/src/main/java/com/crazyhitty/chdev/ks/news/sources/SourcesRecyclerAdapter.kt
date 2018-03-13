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
class SourcesRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_TYPE_TITLE_SELECTED = 1
        const val VIEW_TYPE_TITLE_UNSELECTED = 2
        const val VIEW_TYPE_SOURCE_SELECTED = 3
        const val VIEW_TYPE_SOURCE_UNSELECTED = 4
    }

    private var itemsSelected: Boolean = false

    var selectedSourceItems: ArrayList<SourceItem?> = ArrayList()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    var unselectedSourceItems: ArrayList<SourceItem?> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onCheckBoxClickListener: ((Boolean, SourceItem?) -> Unit)? = null

    fun clear() {
        selectedSourceItems.clear()
        unselectedSourceItems.clear()
        notifyDataSetChanged()
    }

    fun clearSelectedSources() {
        selectedSourceItems.clear()
        notifyDataSetChanged()
    }

    fun clearUnselectedSources() {
        unselectedSourceItems.clear()
        notifyDataSetChanged()
    }

    fun removeFromSelectedSources(position: Int) {
        selectedSourceItems.removeAt(position)
        notifyItemRemoved(position.plus(1))
    }

    fun removeFromUnselectedSources(position: Int) {
        unselectedSourceItems.removeAt(position)
        if (itemsSelected) {
            notifyItemRemoved(position.plus(2).plus(selectedSourceItems.size))
        } else {
            notifyItemRemoved(position)
        }
    }

    fun addToSelectedSources(sourceItem: SourceItem?) {
        selectedSourceItems.add(sourceItem)
        notifyItemInserted(selectedSourceItems.size.plus(1))
    }

    fun addToUnselectedSources(sourceItem: SourceItem?) {
        unselectedSourceItems.add(sourceItem)
        if (itemsSelected) {
            notifyItemInserted(selectedSourceItems.size.plus(2).plus(unselectedSourceItems.size))
        } else {
            notifyItemInserted(unselectedSourceItems.size)
        }
    }

    fun hideHeading() {
        if (selectedSourceItems.size != 0) throw IllegalStateException("Cannot hide heading as " +
                "selected source items are still available")

        itemsSelected = false
        notifyItemRangeRemoved(0, 2)
    }

    fun showHeading() {
        if (selectedSourceItems.size == 0) throw IllegalStateException("Cannot show heading as " +
                "no selected source items are available")

        itemsSelected = true
        notifyItemInserted(0)
        notifyItemInserted(selectedSourceItems.size.plus(1))
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_source, parent, false)
        return SourceViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (holder) {
            is SourceHeadingViewHolder -> {
                val titleSelected = getItemViewType(position) == VIEW_TYPE_TITLE_SELECTED
                onBindTitleViewHolder(holder, titleSelected)
            }
            is SourceViewHolder -> {
                val sourceSelected = getItemViewType(position) == VIEW_TYPE_SOURCE_SELECTED
                val sourceItemPosition = when (getItemViewType(position)) {
                    VIEW_TYPE_SOURCE_SELECTED -> {
                        position.minus(1)
                    }
                    VIEW_TYPE_SOURCE_UNSELECTED -> {
                        if (itemsSelected) {
                            position.minus(2).minus(selectedSourceItems.size)
                        }
                        else {
                            position
                        }
                    }
                    else -> throw IllegalStateException("Invalid source view type, current view type: ${getItemViewType(position)}")
                }
                onBindSourceViewHolder(holder, sourceItemPosition, sourceSelected)
            }
        }
    }

    private fun onBindTitleViewHolder(holder: SourceHeadingViewHolder?, selected: Boolean) {
        val title = if (selected) "Selected" else "Available"

        holder?.textViewHeading?.text = title
    }

    private fun onBindSourceViewHolder(holder: SourceViewHolder?, position: Int, selected: Boolean) {
        val sourceItem = if (selected) selectedSourceItems[position] else unselectedSourceItems[position]

        holder?.textViewName?.text = sourceItem?.name
        holder?.textViewDesc?.text = sourceItem?.description
        holder?.textViewExtraDetails?.text = "${sourceItem?.category} / ${sourceItem?.language} / ${sourceItem?.country}"
        holder?.checkBox?.checked = sourceItem?.selected ?: false
    }

    override fun getItemCount(): Int {
        if (itemsSelected) {
            return selectedSourceItems.size.plus(unselectedSourceItems.size).plus(2)
        }
        return selectedSourceItems.size.plus(unselectedSourceItems.size)
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 && itemsSelected -> VIEW_TYPE_TITLE_SELECTED
            position == 0 && !itemsSelected -> VIEW_TYPE_SOURCE_UNSELECTED
            position >= 1 && position < selectedSourceItems.size -> VIEW_TYPE_SOURCE_SELECTED
            position == selectedSourceItems.size.plus(1) && itemsSelected -> VIEW_TYPE_TITLE_UNSELECTED
            else -> VIEW_TYPE_SOURCE_UNSELECTED
        }
    }

    inner class SourceHeadingViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val textViewHeading = itemView?.find<TextView>(R.id.textViewHeading)
    }

    inner class SourceViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val textViewName = itemView?.find<TextView>(R.id.textViewName)
        val textViewDesc = itemView?.find<TextView>(R.id.textViewDesc)
        val textViewExtraDetails = itemView?.find<TextView>(R.id.textViewExtraDetails)
        val checkBox = itemView?.find<SourceCheckbox>(R.id.checkBox)

        init {
            checkBox?.setOnClickListener {
                val sourceItem = when (getItemViewType(adapterPosition)) {
                    VIEW_TYPE_SOURCE_SELECTED -> {
                        selectedSourceItems[adapterPosition.minus(1)]
                    }
                    VIEW_TYPE_SOURCE_UNSELECTED -> {
                        if (itemsSelected) {
                            unselectedSourceItems[adapterPosition.minus(2).minus(selectedSourceItems.size)]
                        } else {
                            unselectedSourceItems[adapterPosition]
                        }
                    }
                    else -> throw IllegalStateException("Invalid source view type, current view type: ${getItemViewType(adapterPosition)}")
                }
                onCheckBoxClickListener?.invoke(checkBox.checked, sourceItem)
            }
        }
    }
}