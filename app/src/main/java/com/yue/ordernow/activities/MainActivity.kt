package com.yue.ordernow.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.yue.ordernow.R
import com.yue.ordernow.data.OrderItem
import com.yue.ordernow.dialog.AddNoteDialog
import kotlinx.android.synthetic.main.app_bar_main.*

private const val CONFIRM_ORDERS = 1000
const val ORDERS = "orderItems"

class MainActivity : AppCompatActivity(), AddNoteDialog.AddNoteDialogListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    val orderItems = ArrayList<OrderItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_restaurant_menu, R.id.nav_order_history
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CONFIRM_ORDERS) {
            if (resultCode == Activity.RESULT_OK) {
                orderItems.clear()
                orderItems.addAll(data!!.getParcelableArrayListExtra(ORDERS))
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean = when (item.itemId) {
        R.id.action_confirm -> {
            startOrderActivity()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun addOrder(orderItem: OrderItem) {
        for (it in orderItems) {
            if (it.item == orderItem.item && it.note == orderItem.note) {

                // combine the two orderItems
                it.quantity += orderItem.quantity
                return
            }
        }

        orderItems.add(orderItem)
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, orderItem: OrderItem) {
        addOrder(orderItem)
    }

    private fun startOrderActivity() {
        val orderActivityIntent = OrderActivity.getStartActivityIntent(this)
        orderActivityIntent.putParcelableArrayListExtra(ORDERS, orderItems)
        startActivityForResult(orderActivityIntent, CONFIRM_ORDERS)
    }
}
