package com.yue.ordernow.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yue.ordernow.data.OrderItem
import com.yue.ordernow.databinding.ListItemOrderItemBinding
import com.yue.ordernow.databinding.ListItemOrderItemWithNoteBinding

class OrderItemAdapter : ListAdapter<OrderItem, RecyclerView.ViewHolder>(OrderItemDiffCallback()) {

    companion object {
        const val TYPE_WITHOUT_NOTE = 0
        const val TYPE_WITH_NOTE = 1
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position).note) {
        "" -> TYPE_WITHOUT_NOTE
        else -> TYPE_WITH_NOTE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == TYPE_WITHOUT_NOTE) {
            OrderItemViewHolder(
                ListItemOrderItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            OrderItemWithNoteViewHolder(
                ListItemOrderItemWithNoteBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val menuItem = getItem(position)
        if (getItemViewType(position) == TYPE_WITHOUT_NOTE) {
            (holder as OrderItemViewHolder).bind(menuItem)
        } else {
            (holder as OrderItemWithNoteViewHolder).bind(menuItem)
        }

    }

    private inner class OrderItemViewHolder(private val binding: ListItemOrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderItem) {
            binding.apply {
                orderItem = item
                executePendingBindings()
            }
        }
    }

    private inner class OrderItemWithNoteViewHolder(private val binding: ListItemOrderItemWithNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderItem) {
            binding.apply {
                orderItem = item
                executePendingBindings()
            }
        }
    }
}

private class OrderItemDiffCallback : DiffUtil.ItemCallback<OrderItem>() {

    override fun areItemsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean =
        oldItem == newItem
}