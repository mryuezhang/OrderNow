package com.yue.ordernow.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yue.ordernow.R
import com.yue.ordernow.models.MenuItem

class OrderListAdapter(private val items: ArrayList<MenuItem>) :
    RecyclerView.Adapter<OrderListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false)
        return OrderListViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: OrderListViewHolder, position: Int) {
        holder.itemName.text = items[position].name
//        holder.orderCount.text = items[position].orderCount.toString()
//        holder.totalAmount.text = currencyFormat((items[position].totalAmount()))
    }
}