package com.yue.ordernow

import android.app.Application
import com.yue.ordernow.data.AppDatabase
import com.yue.ordernow.data.Order

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val database = AppDatabase.getInstance(applicationContext)
        val lastOrder = database.orderDao().getLastOrder().value
        if (lastOrder != null) {
            Order.lastOrderCreatedTime = lastOrder.timeCreated
            Order.orderCount = lastOrder.orderNumber
        } else {
            Order.lastOrderCreatedTime = null
            Order.orderCount = 0
        }
    }
}