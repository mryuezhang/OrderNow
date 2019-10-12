package com.yue.ordernow.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yue.ordernow.R
import com.yue.ordernow.data.OrderItem
import com.yue.ordernow.fragments.NoOrderFragment
import com.yue.ordernow.fragments.OrderListFragment
import kotlinx.android.synthetic.main.activity_order.*
import java.util.*


class OrderActivity : AppCompatActivity(),
    OrderListFragment.OnOrderListFragmentInteractionListener {
    private var orders: ArrayList<OrderItem>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Get orders from intent
        orders = intent.getParcelableArrayListExtra(ORDERS)

        if (orders.isNullOrEmpty()) {

            // Inform users that there is no orders
            val noOrderFragment = NoOrderFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, noOrderFragment).commit()
        } else {
            var subtotalAmount = 0F
            var totalQuantity = 0

            // Calculate subtotal and total quantity
            orders!!.forEach { orderItem ->
                subtotalAmount += orderItem.item.price
                totalQuantity += orderItem.quantity
            }

            // Sort all order items
            orders!!.sortWith(Comparator { t, t2 ->
                t.item.name.compareTo(t2.item.name)
            })

            // Display all orders
            val orderListFragment =
                OrderListFragment.newInstance(orders!!, subtotalAmount)
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, orderListFragment).commit()
        }
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent()
                intent.putParcelableArrayListExtra(ORDERS, orders)
                setResult(Activity.RESULT_OK, intent)
                finish()
                return true
            }
            R.id.action_send -> {
//                Log.i(tag, order.toString())
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