package com.yue.ordernow.models

import java.util.*

data class Order(val orderItems: ArrayList<OrderItem>) {
    val timeCreated = Calendar.getInstance()

    var orderNumber: Int = if (timeCreated.get(Calendar.DAY_OF_MONTH) ==
        lastOrderCreatedTime?.get(Calendar.DAY_OF_MONTH)
    ) { // if this and previous order are made within the same day
        lastOrderCreatedTime = this.timeCreated
        ++orderCount
    } else { // if this order and previous order are not made within the same day, or there is no previous order
        lastOrderCreatedTime = this.timeCreated
        orderCount = 0
        ++orderCount
    }

    override fun toString(): String =
        "#${orderNumber}, created at ${timeCreated.time}, ${orderItems}"

    companion object {
        var lastOrderCreatedTime: Calendar? = null
        var orderCount = 0
    }
}