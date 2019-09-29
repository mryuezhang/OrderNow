package com.yue.ordernow.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.yue.ordernow.R
import com.yue.ordernow.models.MenuItem
import com.yue.ordernow.utils.currencyFormat

class MenuListAdapter(private val itemList: ArrayList<MenuItem>) :
    RecyclerView.Adapter<MenuListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_item, parent, false)
        return MenuListViewHolder(view)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: MenuListViewHolder, position: Int) {
        holder.itemNameTextView.text = itemList[position].name
        holder.orderCountEditText.setText(itemList[position].orderCount.toString())
        holder.priceTextView.text = currencyFormat(itemList[position].price)

        holder.addButton.setOnClickListener {
            if (itemList[position].orderCount < 999) {
                itemList[position].orderCount++
                holder.orderCountEditText.setText(itemList[position].orderCount.toString())
            }
        }

        holder.removeButton.setOnClickListener {
            if (itemList[position].orderCount > 0) {
                itemList[position].orderCount--
                holder.orderCountEditText.setText(itemList[position].orderCount.toString())
            }
        }

        holder.orderCountEditText.doOnTextChanged { text, _, _, _ ->
            if (text.toString() != "") {
                val numericValue = text.toString().toInt()
                if (numericValue in 0..999) {
                    itemList[position].orderCount = numericValue
                }
            }
        }
    }
}