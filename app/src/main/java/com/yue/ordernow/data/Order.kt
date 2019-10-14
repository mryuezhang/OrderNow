package com.yue.ordernow.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "orders")
data class Order(
    @ColumnInfo(name = "order-items")
    var orderItems: ArrayList<OrderItem>,

    @ColumnInfo(name = "subtotal")
    var subtotalAmount: Float,

    @ColumnInfo(name = "total-quantity")
    var totalQuantity: Int,

    @ColumnInfo(name = "order-number")
    var orderNumber: Int
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var orderId: Long = 0

    @ColumnInfo(name = "time-created")
    var timeCreated: Calendar = Calendar.getInstance()

//    @ColumnInfo(name = "order-number")
//    var orderNumber: Int = if (timeCreated.get(Calendar.DAY_OF_MONTH) ==
//        lastOrderCreatedTime?.get(Calendar.DAY_OF_MONTH)
//    ) { // if this and previous order are made within the same day
//        lastOrderCreatedTime = this.timeCreated
//        ++orderCount
//    } else { // if this order and previous order are not made within the same day, or there is no previous order
//        lastOrderCreatedTime = this.timeCreated
//        orderCount = 0
//        ++orderCount
//    }

    companion object {
        var lastOrderCreatedTime: Calendar? = null
        var orderCount = 0

        fun newInstance(
            orderItems: ArrayList<OrderItem>,
            subtotal: Float,
            totalQuantity: Int
        ): Order {
            val now = Calendar.getInstance()
            if (now.get(Calendar.DAY_OF_MONTH) ==
                lastOrderCreatedTime?.get(Calendar.DAY_OF_MONTH)
            ) { // if this and previous order are made within the same day
                lastOrderCreatedTime = now
                ++orderCount
            } else { // if this order and previous order are not made within the same day, or there is no previous order
                lastOrderCreatedTime = now
                orderCount = 0
                ++orderCount
            }

            return Order(orderItems, subtotal, totalQuantity, orderCount)
        }
    }
}