package com.yue.ordernow.list

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yue.ordernow.R


class OrderWithoutNoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val itemName: TextView = view.findViewById(R.id.item_name)
    val price: TextView = view.findViewById(R.id.amount)
}

class OrderWithNoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val itemName: TextView = view.findViewById(R.id.item_name)
    val note: TextView = view.findViewById(R.id.note)
    val price: TextView = view.findViewById(R.id.amount)
}