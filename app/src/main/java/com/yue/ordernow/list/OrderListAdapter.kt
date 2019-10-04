package com.yue.ordernow.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yue.ordernow.R
import com.yue.ordernow.models.Order
import com.yue.ordernow.utils.currencyFormat

class OrderListAdapter(private val orders: ArrayList<Order>) :
    RecyclerView.Adapter<OrderListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false)
        return OrderListViewHolder(view)
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: OrderListViewHolder, position: Int) {
        holder.itemName.text = orders[position].item.name
        holder.price.text = currencyFormat(orders[position].item.price)
//        holder.orderCount.text = items[position].orderCount.toString()
//        holder.totalAmount.text = currencyFormat((items[position].totalAmount()))
    }
}