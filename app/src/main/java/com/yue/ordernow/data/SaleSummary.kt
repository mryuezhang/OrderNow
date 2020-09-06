package com.yue.ordernow.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yue.ordernow.data.Report.Type
import com.yue.ordernow.utilities.getDayStart
import com.yue.ordernow.utilities.getMonthStart
import com.yue.ordernow.utilities.getWeekStart
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.forEach
import kotlin.collections.getValue
import kotlin.collections.set
import kotlin.collections.withDefault

@Entity(tableName = "sale-summary")
data class SaleSummary(
    @ColumnInfo(name = "type")
    var type: Type
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0

    @ColumnInfo(name = "date")
    var date: Calendar = when (type) {
        Type.TODAY -> { getDayStart(Calendar.getInstance()) }
        Type.THIS_WEEK -> { getWeekStart(Calendar.getInstance()) }
        Type.THIS_MONTH -> { getMonthStart(Calendar.getInstance()) }
    }

    @ColumnInfo(name = "order-ids")
    var orderIds = ArrayList<Long>()

    @ColumnInfo(name = "sale-data")
    var saleData = HashMap<String, Int>().withDefault { 0 } // <Menu Item Name, # of sold>

    private fun addSaleData(string: String, quantity: Int = 1) {
        saleData[string] = saleData.getValue(string) + quantity
    }

    private fun addSaleData(menuItem: MenuItem, quantity: Int = 1) {
        addSaleData(menuItem.name, quantity)
    }

    private fun addSaleData(orderItem: OrderItem) {
        addSaleData(orderItem.item, orderItem.quantity)
    }

    fun addSaleData(order: Order) {
        if (!orderIds.contains(order.id)) {
            order.orderItems.forEach {
                addSaleData(it)
            }
            orderIds.add(order.id)
        }
    }

    private fun removeSaleData(string: String, quantity: Int = 1) {
        val v = saleData.getValue(string)
        if (v > quantity) {
            saleData[string] = v - quantity

        } else if (v == quantity) {
            saleData.remove(string)
        }
    }

    private fun removeSaleData(menuItem: MenuItem, quantity: Int = 1) {
        removeSaleData(menuItem.name, quantity)
    }

    private fun removeSaleData(orderItem: OrderItem) {
        removeSaleData(orderItem.item, orderItem.quantity)
    }

    fun removeSaleData(order: Order) {
        if (orderIds.contains(order.id)) {
            order.orderItems.forEach {
                removeSaleData(it)
            }
            orderIds.remove(order.id)
        }
    }
}