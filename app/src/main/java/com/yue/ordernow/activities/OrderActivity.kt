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
import com.yue.ordernow.fragments.NoOrderFragment
import com.yue.ordernow.fragments.OrderListFragment
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

        if (orders.isEmpty()) {
            val noOrderFragment = NoOrderFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, noOrderFragment).commit()
        } else {
            val orderListFragment = OrderListFragment.newInstance(orders, total)
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, orderListFragment).commit()
        }

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