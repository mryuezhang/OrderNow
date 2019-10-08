package com.yue.ordernow.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yue.ordernow.R
import com.yue.ordernow.data.OrderItem
import com.yue.ordernow.utils.currencyFormat

class OrderListAdapter(private val orderItems: ArrayList<OrderItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_WITHOUT_NOTE = 0
        const val TYPE_WITH_NOTE = 1
    }

    override fun getItemViewType(position: Int): Int = when (orderItems[position].note) {
        "" -> TYPE_WITHOUT_NOTE
        else -> TYPE_WITH_NOTE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == TYPE_WITHOUT_NOTE) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false)
            OrderWithoutNoteViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.order_item_with_note, parent, false)
            OrderWithNoteViewHolder(view)
        }

    override fun getItemCount(): Int = orderItems.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is OrderWithoutNoteViewHolder -> {
                holder.itemName.text = orderItems[position].item.name
                holder.quantity.text = orderItems[position].quantity.toString()
                holder.unitPrice.text = currencyFormat(orderItems[position].item.price)
                holder.amount.text =
                    currencyFormat(orderItems[position].item.price * orderItems[position].quantity)
            }
            is OrderWithNoteViewHolder -> {
                holder.itemName.text = orderItems[position].item.name
                holder.quantity.text = orderItems[position].quantity.toString()
                holder.unitPrice.text = currencyFormat(orderItems[position].item.price)
                holder.amount.text =
                    currencyFormat(orderItems[position].item.price * orderItems[position].quantity)
                holder.note.text = orderItems[position].note
            }
            else -> throw IllegalStateException("Unknown view holder type")
        }
    }
}