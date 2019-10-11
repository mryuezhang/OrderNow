package com.yue.ordernow.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yue.ordernow.activities.MainActivity
import com.yue.ordernow.data.MenuItem
import com.yue.ordernow.data.OrderItem
import com.yue.ordernow.databinding.MenuItemBinding
import com.yue.ordernow.dialog.AddNoteDialog

class MenuItemAdapter(val activity: FragmentActivity) :
    ListAdapter<MenuItem, RecyclerView.ViewHolder>(MenuItemDiffCallback()) {

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
                if (activity is MainActivity) {
                    activity.addOrder(OrderItem(binding.menuItem!!, 1, ""))
                } else {
                    throw IllegalArgumentException("context must be MainActivity")
                }
            }

            binding.setCustomizeButtonOnclickListener {
                AddNoteDialog(binding.menuItem!!.copy()) // MUST pass a copy here
                    .show(activity.supportFragmentManager, "")
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