package com.yue.ordernow.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yue.ordernow.data.ReportItem
import com.yue.ordernow.databinding.ListItemReportBinding

class ReportItemAdapter :
    ListAdapter<ReportItem, RecyclerView.ViewHolder>(ReportItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ReportItemViewHolder(
            ListItemReportBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val reportItem = getItem(position)
        (holder as ReportItemViewHolder).bind(reportItem)
    }

    private inner class ReportItemViewHolder(private val binding: ListItemReportBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ReportItem) {
            binding.apply {
                report = item
                executePendingBindings()
            }
        }
    }

    private class ReportItemDiffCallback : DiffUtil.ItemCallback<ReportItem>() {

        override fun areItemsTheSame(oldItem: ReportItem, newItem: ReportItem): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: ReportItem, newItem: ReportItem): Boolean =
            oldItem == newItem
    }
}