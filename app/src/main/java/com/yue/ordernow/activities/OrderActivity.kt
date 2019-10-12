package com.yue.ordernow.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.yue.ordernow.R
import com.yue.ordernow.data.Order
import com.yue.ordernow.data.OrderItem
import com.yue.ordernow.databinding.ActivityOrderBinding
import com.yue.ordernow.fragments.NoOrderFragment
import com.yue.ordernow.fragments.OrderListFragment
import kotlinx.android.synthetic.main.activity_order.*
import java.util.*


class OrderActivity : AppCompatActivity(),
    OrderListFragment.OnOrderListFragmentInteractionListener {
    private var orders: ArrayList<OrderItem>? = null
    private var order: Order? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView<ActivityOrderBinding>(this, R.layout.activity_order)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Get orders from intent
        orders = intent.getParcelableArrayListExtra(ORDERS)

        if (orders.isNullOrEmpty()) {

            // Inform users that there is no orders
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, NoOrderFragment()).commit()
        } else {
            var subtotalAmount = 0F
            var totalQuantity = 0

            // Calculate subtotal and total quantity
            orders!!.forEach { orderItem ->
                subtotalAmount += orderItem.item.price * orderItem.quantity
                totalQuantity += orderItem.quantity
            }

            // Sort all order items
            orders!!.sortWith(Comparator { t, t2 ->
                t.item.name.compareTo(t2.item.name)
            })

            // Create Order object
            order = Order(orders!!, subtotalAmount, totalQuantity)

            // Display all orders
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.fragment_container,
                    OrderListFragment.newInstance(orders!!, subtotalAmount)
                ).commit()
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
//                order?.let {
//                    OrderRepository.getInstance().insertOrder(it)
//                }

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