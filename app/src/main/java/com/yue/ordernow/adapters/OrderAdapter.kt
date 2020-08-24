package com.yue.ordernow.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yue.ordernow.R
import com.yue.ordernow.data.Order
import com.yue.ordernow.databinding.ListHeaderOrderHistoryBinding
import com.yue.ordernow.databinding.ListItemOrderHistoryBinding
import com.yue.ordernow.fragments.ReportDetailFragmentDirections

class OrderAdapter(
    private val context: Context,
    private val listener: ItemLongClickListener
) :
    ListAdapter<OrderAdapter.ListItem, RecyclerView.ViewHolder>(OrderDiffCallback()) {

    private companion object {
        const val TYPE_ORDER = 0
        const val TYPE_HEADER = 1
    }

    interface ListItem

    interface ItemLongClickListener {
        fun onLongClick(order: Order, adapter: OrderAdapter, position: Int)
    }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is Order -> {
                TYPE_ORDER
            }
            else -> {
                TYPE_HEADER
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            TYPE_ORDER -> OrderViewHolder(
                ListItemOrderHistoryBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> HeaderViewHolder(
                ListHeaderOrderHistoryBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is Order -> {
                (holder as OrderViewHolder).bind(item)
            }
            is Header -> {
                (holder as HeaderViewHolder).bind(item.text)
            }
            else -> {
                Log.e(OrderAdapter::javaClass.name, "Unknown list item type")
            }
        }
    }

    data class Header(val text: String) : ListItem

    private inner class OrderViewHolder(private val binding: ListItemOrderHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setOnClickListener {
                binding.order?.let { order ->
                    navigateToOrder(order, it)
                }
            }
            binding.mainLayout.setOnLongClickListener {
                binding.order?.let { order ->
                    listener.onLongClick(order, this@OrderAdapter, adapterPosition)
                }
                true
            }
        }

        fun bind(item: Order) {
            binding.apply {
                order = item
                executePendingBindings()
                this.orderType.text = if (item.isTakeout) {
                    context.getString(R.string.take_out)
                } else {
                    context.getString(R.string.dining_in)
                }
                this.unpaidIcon.visibility = if (item.isPaid) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }
        }

        private fun navigateToOrder(
            order: Order,
            it: View
        ) {
            val direction =
                ReportDetailFragmentDirections.actionToOrderDetailFragment(
                    order
                )

            it.findNavController().navigate(direction)
        }
    }

    private class HeaderViewHolder(private val binding: ListHeaderOrderHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(text: String) {
            binding.apply {
                this.orderHistoryListBreaker.text = text
            }
        }
    }
}

private class OrderDiffCallback : DiffUtil.ItemCallback<OrderAdapter.ListItem>() {

    override fun areItemsTheSame(
        oldItem: OrderAdapter.ListItem,
        newItem: OrderAdapter.ListItem
    ): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(
        oldItem: OrderAdapter.ListItem,
        newItem: OrderAdapter.ListItem
    ): Boolean =
        when {
            oldItem is OrderAdapter.Header && newItem is OrderAdapter.Header -> oldItem.text == newItem.text
            oldItem is Order && newItem is Order -> oldItem.toString() == newItem.toString()
            else -> false
        }
}

