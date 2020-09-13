package com.yue.ordernow

import android.app.Application
import com.yue.ordernow.data.AppDatabase
import com.yue.ordernow.data.Order

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val database = AppDatabase.getInstance(applicationContext)
        database.orderDao().getLastOrder().observeForever {
            if (it != null) {
                Order.lastOrderCreatedTime = it.timeCreated
                Order.orderCount = it.orderNumber
            }
        }
    }
}