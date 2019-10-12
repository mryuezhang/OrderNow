package com.yue.ordernow.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yue.ordernow.data.MenuItem
import com.yue.ordernow.databinding.MenuItemBinding

class MenuItemAdapter(private val listener: MenuItemListener) :
    ListAdapter<MenuItem, RecyclerView.ViewHolder>(MenuItemDiffCallback()) {

    interface MenuItemListener {
        fun onOrderButtonClick(menuItem: MenuItem?)
        fun onCustomizeButtonClick(menuItem: MenuItem?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MenuItemViewHolder(
            MenuItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val menuItem = getItem(position)
        (holder as MenuItemViewHolder).bind(menuItem)
    }

    private inner class MenuItemViewHolder(private val binding: MenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setOrderButtonOnClickListener {
                listener.onOrderButtonClick(binding.menuItem)
            }

            binding.setCustomizeButtonOnclickListener {
                listener.onCustomizeButtonClick(binding.menuItem)
            }
        }

        fun bind(item: MenuItem) {
            binding.apply {
                menuItem = item
                executePendingBindings()
            }
        }
    }
}

private class MenuItemDiffCallback : DiffUtil.ItemCallback<MenuItem>() {

    override fun areItemsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean =
        oldItem == newItem
}