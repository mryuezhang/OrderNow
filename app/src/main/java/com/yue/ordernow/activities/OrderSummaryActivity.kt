package com.yue.ordernow.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.yue.ordernow.R
import com.yue.ordernow.data.Order
import com.yue.ordernow.databinding.ActivityOrderBinding
import com.yue.ordernow.fragments.NoOrderFragment
import com.yue.ordernow.fragments.OrderListFragment
import com.yue.ordernow.utils.OrderSummaryActivityArgs
import kotlinx.android.synthetic.main.activity_order.*
import java.util.*


class OrderSummaryActivity : AppCompatActivity(),
    OrderListFragment.OnOrderListFragmentInteractionListener {
    private val args by lazy {
        OrderSummaryActivityArgs.create(intent)
    }

    private val order: Order by lazy {
        var subtotalAmount = 0F
        var totalQuantity = 0

        // Calculate subtotal and total quantity
        args.orderItems.forEach { orderItem ->
            subtotalAmount += orderItem.item.price * orderItem.quantity
            totalQuantity += orderItem.quantity
        }

        // Sort all order items
        args.orderItems.sortWith(Comparator { t, t2 ->
            t.item.name.compareTo(t2.item.name)
        })

        // Create Order object
        Order(args.orderItems, subtotalAmount, totalQuantity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView<ActivityOrderBinding>(this, R.layout.activity_order)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (args.orderItems.isNullOrEmpty()) {
            // Inform users that there is no orders
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, NoOrderFragment()).commit()
        } else {
            // Display all orders
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.fragment_container,
                    OrderListFragment.newInstance(args.orderItems, order.subtotalAmount)
                ).commit()
        }
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent()
                intent.putParcelableArrayListExtra(ORDERS, args.orderItems)
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
            Intent(context, OrderSummaryActivity::class.java)

        private const val tag = "OrderSummaryActivity"
    }
}