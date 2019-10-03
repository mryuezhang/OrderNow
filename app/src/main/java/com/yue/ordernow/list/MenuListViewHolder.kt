package com.yue.ordernow.list

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yue.ordernow.R

class MenuListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val itemNameTextView: TextView = view.findViewById(R.id.item_name)
    val priceTextView: TextView = view.findViewById(R.id.price)
    val orderButton: Button = view.findViewById(R.id.button_order)
    val addNoteButton: Button = view.findViewById(R.id.button_add_note)
}