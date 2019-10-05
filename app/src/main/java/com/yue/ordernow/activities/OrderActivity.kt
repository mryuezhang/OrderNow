package com.yue.ordernow.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yue.ordernow.R
import com.yue.ordernow.fragments.NoOrderFragment
import com.yue.ordernow.fragments.OrderListFragment
import com.yue.ordernow.models.Order
import kotlinx.android.synthetic.main.activity_order.*
import java.util.*
import kotlin.collections.ArrayList


class OrderActivity : AppCompatActivity(),
    OrderListFragment.OnOrderListFragmentInteractionListener {
    private val orders = ArrayList<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var total = 0.0F

        orders.addAll(intent.getParcelableArrayListExtra(ORDERS))
        orders.forEach {
            total += it.item.price
        }
        orders.sortWith(Comparator { t, t2 ->
            t.item.name.compareTo(t2.item.name)
        })

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
                val intent = Intent()
                intent.putParcelableArrayListExtra(ORDERS, orders)
                setResult(Activity.RESULT_OK, intent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onOrderListFragmentInteraction() {

        val noOrderFragment = NoOrderFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, noOrderFragment).commit()

//        enumValues<RestaurantMenuFragment.Companion.Category>().forEach { category ->
//            menuItems[category]?.forEach { menuItem ->
//                menuItem.orderCount = 0
//            }
//        }
    }

    companion object {
        fun getStartActivityIntent(context: Context) =
            Intent(context, OrderActivity::class.java)
    }
}