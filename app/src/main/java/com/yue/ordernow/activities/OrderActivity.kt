package com.yue.ordernow.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.yue.ordernow.R
import com.yue.ordernow.list.OrderListAdapter
import com.yue.ordernow.models.MenuItem
import com.yue.ordernow.utils.currencyFormat
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.android.synthetic.main.content_order.*


class OrderActivity : AppCompatActivity() {

    companion object {
        fun getStartActivityIntent(context: Context) =
            Intent(context, OrderActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val orders = ArrayList<MenuItem>()
        var total = 0.0F

        enumValues<MainActivity.Companion.Category>().forEach { category ->
            intent.getParcelableArrayListExtra<MenuItem>(category.name).forEach { menuItem ->
                if (menuItem.orderCount > 0) {
                    Log.i("FUCK", menuItem.toString())
                    orders.add(menuItem)
                    total += menuItem.totalAmount()
                }
            }

        }

        orderList.layoutManager = LinearLayoutManager(this)
        orderList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        orderList.adapter = OrderListAdapter(orders)
        totalAmount.text = currencyFormat(total)
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}