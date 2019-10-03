package com.yue.ordernow.list

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yue.ordernow.R

class OrderListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val itemName: TextView = view.findViewById(R.id.item_name)
    val orderCount: TextView = view.findViewById(R.id.orderCount)
    val totalAmount: TextView = view.findViewById(R.id.amount)
}