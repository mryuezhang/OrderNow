package com.yue.ordernow.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.yue.ordernow.R
import com.yue.ordernow.adapters.MenuItemAdapter
import com.yue.ordernow.data.MenuItem
import com.yue.ordernow.data.OrderItem
import com.yue.ordernow.databinding.ActivityMainBinding
import com.yue.ordernow.fragments.AddNoteDialogFragment
import com.yue.ordernow.utils.OrderSummaryActivityArgs
import com.yue.ordernow.utils.OrderSummaryActivityArgs.Companion.ARG_ORDERS
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*


private const val CONFIRM_ORDERS = 1000

class MainActivity : AppCompatActivity(), AddNoteDialogFragment.AddNoteDialogListener,
    MenuItemAdapter.MenuItemListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val orderItems = ArrayList<OrderItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        setSupportActionBar(toolbar)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_restaurant_menu, R.id.nav_order_history
            ), binding.drawerLayout
        )

        val navController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CONFIRM_ORDERS) {
            if (resultCode == Activity.RESULT_OK) {
                orderItems.clear()
                orderItems.addAll(data!!.getParcelableArrayListExtra(ARG_ORDERS))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean = when (item.itemId) {
        R.id.action_confirm -> {
            OrderSummaryActivityArgs(orderItems).launchForResult(this, CONFIRM_ORDERS)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun addOrder(orderItem: OrderItem) {
        for (it in orderItems) {
            if (it.item == orderItem.item && it.note == orderItem.note) {

                // combine the two orderItems
                it.quantity += orderItem.quantity
                return
            }
        }

        orderItems.add(orderItem)
    }

    private fun showShortSnackbar(message: String) {
        Snackbar.make(drawer_layout, message, Snackbar.LENGTH_SHORT)
            .show()
    }

    //  AddNoteDialogFragment.AddNoteDialogListener method

    override fun onDialogPositiveClick(dialog: DialogFragment, orderItem: OrderItem) {
        addOrder(orderItem)

        if (orderItem.quantity == 1) {
            showShortSnackbar("Item has been added to order")
        } else {
            showShortSnackbar("Items have been added to order")
        }
    }

    // MenuItemAdapter.MenuItemListener methods

    override fun onOrderButtonClick(menuItem: MenuItem?) {
        menuItem?.let {
            addOrder(OrderItem(it, 1, ""))
            showShortSnackbar("Item has been added to order")
        }
    }

    override fun onCustomizeButtonClick(menuItem: MenuItem?) {
        menuItem?.let {
            AddNoteDialogFragment(it.copy()) // MUST pass a copy here
                .show(supportFragmentManager, "")
        }
    }
}
