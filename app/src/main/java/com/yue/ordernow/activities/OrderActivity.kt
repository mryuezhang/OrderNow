package com.yue.ordernow.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yue.ordernow.R
import com.yue.ordernow.fragments.NoOrderFragment
import com.yue.ordernow.fragments.OrderListFragment
import com.yue.ordernow.fragments.RestaurantMenuFragment
import com.yue.ordernow.models.MenuItem
import kotlinx.android.synthetic.main.activity_order.*


class OrderActivity : AppCompatActivity(),
    OrderListFragment.OnOrderListFragmentInteractionListener {
    private val menuItems =
        HashMap<RestaurantMenuFragment.Companion.Category, ArrayList<MenuItem>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val orders = ArrayList<MenuItem>()
        var total = 0.0F

        enumValues<RestaurantMenuFragment.Companion.Category>().forEach { category ->
            menuItems[category] = intent.getParcelableArrayListExtra<MenuItem>(category.name)
//            menuItems[category]!!.forEach { menuItem ->
//                if (menuItem.orderCount > 0) {
//                    orders.add(menuItem)
//                    total += menuItem.totalAmount()
//                }
//            }
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
                val intent = Intent()
                enumValues<RestaurantMenuFragment.Companion.Category>().forEach { category ->
                    intent.putParcelableArrayListExtra(category.name, menuItems[category])
                }
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