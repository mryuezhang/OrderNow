package com.yue.ordernow.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.yue.ordernow.R
import com.yue.ordernow.data.Order
import com.yue.ordernow.fragments.NoOrderFragment
import com.yue.ordernow.fragments.OrderListFragment
import kotlinx.android.synthetic.main.activity_order.*
import java.util.*


class OrderActivity : AppCompatActivity(),
    OrderListFragment.OnOrderListFragmentInteractionListener {
    private var order: Order? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        order = Order(intent.getParcelableArrayListExtra(ORDERS))

        // calculate subtotal, and total quantity
        order!!.orderItems.forEach {
            order!!.subtotalAmount += it.item.price
            order!!.totalQuantity += it.quantity
        }

        // sort items in alphabetical order
        order!!.orderItems.sortWith(Comparator { t, t2 ->
            t.item.name.compareTo(t2.item.name)
        })

        if (order!!.orderItems.isEmpty()) {
            val noOrderFragment = NoOrderFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, noOrderFragment).commit()
        } else {
            val orderListFragment =
                OrderListFragment.newInstance(order!!.orderItems, order!!.subtotalAmount)
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, orderListFragment).commit()
        }
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent()
                intent.putParcelableArrayListExtra(ORDERS, order!!.orderItems)
                setResult(Activity.RESULT_OK, intent)
                finish()
                return true
            }
            R.id.action_send -> {
                Log.i(tag, order.toString())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onOrderListFragmentInteraction() {

        val noOrderFragment = NoOrderFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, noOrderFragment).commit()
    }

    companion object {
        fun getStartActivityIntent(context: Context) =
            Intent(context, OrderActivity::class.java)

        private const val tag = "OrderActivity"
    }
}