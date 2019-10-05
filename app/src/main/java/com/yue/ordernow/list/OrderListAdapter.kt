package com.yue.ordernow.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yue.ordernow.R
import com.yue.ordernow.models.Order
import com.yue.ordernow.utils.currencyFormat

class OrderListAdapter(private val orders: ArrayList<Order>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_WITHOUT_NOTE = 0
        const val TYPE_WITH_NOTE = 1
    }

    override fun getItemViewType(position: Int): Int = when (orders[position].note) {
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

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is OrderWithoutNoteViewHolder -> {
                holder.itemName.text = orders[position].item.name
                holder.quantity.text = orders[position].quantity.toString()
                holder.unitPrice.text = currencyFormat(orders[position].item.price)
                holder.amount.text =
                    currencyFormat(orders[position].item.price * orders[position].quantity)
            }
            is OrderWithNoteViewHolder -> {
                holder.itemName.text = orders[position].item.name
                holder.quantity.text = orders[position].quantity.toString()
                holder.unitPrice.text = currencyFormat(orders[position].item.price)
                holder.amount.text =
                    currencyFormat(orders[position].item.price * orders[position].quantity)
                holder.note.text = orders[position].note
            }
            else -> throw IllegalStateException("Unknown view holder type")
        }
    }
}