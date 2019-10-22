package com.yue.ordernow.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yue.ordernow.R
import com.yue.ordernow.data.Order
import com.yue.ordernow.databinding.ListItemOrderHistoryBinding

class OrderAdapter(private val context: Context) :
    ListAdapter<Order, RecyclerView.ViewHolder>(OrderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        OrderViewHolder(
            ListItemOrderHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val order = getItem(position)
        (holder as OrderViewHolder).bind(order)
    }

    private inner class OrderViewHolder(private val binding: ListItemOrderHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Order) {
            binding.apply {
                order = item
                executePendingBindings()
                this.orderType.text = if (item.isTakeout) {
                    context.getString(R.string.take_out)
                } else {
                    context.getString(R.string.dining_in)
                }
            }
        }
    }
}

private class OrderDiffCallback : DiffUtil.ItemCallback<Order>() {

    override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean =
        oldItem == newItem
}

