package com.yue.ordernow.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "orders")
data class Order(
    @ColumnInfo(name = "order-items")
    val orderItems: ArrayList<OrderItem>
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var menuItemId: Long = 0

    @ColumnInfo(name = "time-created")
    private val timeCreated: Calendar = Calendar.getInstance()

    @ColumnInfo(name = "subtotal")
    var subtotalAmount = 0.0F

    @ColumnInfo(name = "total-quantity")
    var totalQuantity = 0

    @ColumnInfo(name = "order number")
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

    constructor(orderItems: ArrayList<OrderItem>, subtotal: Float, totalQuantity: Int) : this(
        orderItems
    ) {
        this.subtotalAmount = subtotal
        this.totalQuantity = totalQuantity
    }

    override fun toString(): String =
        "#${orderNumber}, created at ${timeCreated.time}, $orderItems"

    companion object {
        var lastOrderCreatedTime: Calendar? = null
        var orderCount = 0
    }
}