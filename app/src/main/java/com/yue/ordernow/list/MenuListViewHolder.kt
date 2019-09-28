package com.yue.ordernow.list

import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yue.ordernow.R

class MenuListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val itemNameTextView: TextView = view.findViewById(R.id.itemName)
    val priceTextView: TextView = view.findViewById(R.id.price)
    val addButton: ImageButton = view.findViewById(R.id.addButton)
    val removeButton: ImageButton = view.findViewById(R.id.removeButton)
    val orderCountEditText: EditText = view.findViewById(R.id.orderCount)
}