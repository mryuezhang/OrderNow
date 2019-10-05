package com.yue.ordernow.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.yue.ordernow.R
import com.yue.ordernow.activities.MainActivity
import com.yue.ordernow.dialog.AddNoteDialog
import com.yue.ordernow.models.MenuItem
import com.yue.ordernow.models.Order
import com.yue.ordernow.utils.currencyFormat

class MenuListAdapter(
    private val context: FragmentActivity,
    private val itemList: ArrayList<MenuItem>
) :
    RecyclerView.Adapter<MenuListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_item, parent, false)
        return MenuListViewHolder(view)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: MenuListViewHolder, position: Int) {
        holder.itemNameTextView.text = itemList[position].name
        holder.priceTextView.text = currencyFormat(itemList[position].price)
        holder.orderButton.setOnClickListener {
            if (context is MainActivity) {
                context.addOrder(Order(itemList[position], 1, ""))
            } else {
                throw IllegalArgumentException("context must be MainActivity")
            }
        }

        holder.addNoteButton.setOnClickListener {
            AddNoteDialog(itemList[position].copy()) // MUST pass a copy here
                .show(context.supportFragmentManager, "")
        }
    }
}