package com.yue.ordernow.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.yue.ordernow.R
import com.yue.ordernow.data.Order
import com.yue.ordernow.databinding.ActivityOrderSummaryBinding
import com.yue.ordernow.fragments.NoOrderFragment
import com.yue.ordernow.fragments.OrderListFragment
import com.yue.ordernow.utils.InjectorUtils
import com.yue.ordernow.utils.OrderSummaryActivityArgs
import com.yue.ordernow.utils.OrderSummaryActivityArgs.Companion.ARG_ORDERS
import com.yue.ordernow.viewModels.OrderSummaryViewModel
import kotlinx.android.synthetic.main.activity_order_summary.*
import java.util.*


class OrderSummaryActivity : AppCompatActivity(),
    OrderListFragment.OnOrderListFragmentInteractionListener {

    private val args by lazy {
        OrderSummaryActivityArgs.create(intent)
    }

    private val viewModel: OrderSummaryViewModel by viewModels {
        InjectorUtils.provideOrderSummaryViewModelFactory(this)
    }

    private val order: Order by lazy {
        var subtotalAmount = 0f
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
        Order.newInstance(args.orderItems, subtotalAmount, totalQuantity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView<ActivityOrderSummaryBinding>(this, R.layout.activity_order_summary)
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
                intent.putParcelableArrayListExtra(ARG_ORDERS, args.orderItems)
                setResult(Activity.RESULT_OK, intent)
                finish()
                return true
            }
            R.id.action_send -> {
                viewModel.saveToDatabase(order)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onOrderListFragmentInteraction() {
        val noOrderFragment = NoOrderFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, noOrderFragment).commit()
    }
}