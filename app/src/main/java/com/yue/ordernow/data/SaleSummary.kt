package com.yue.ordernow.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yue.ordernow.data.Report.Type
import com.yue.ordernow.utilities.getDayStart
import com.yue.ordernow.utilities.getMonthStart
import com.yue.ordernow.utilities.getWeekStart
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set

@Entity(tableName = "sale-summary")
data class SaleSummary (
    @ColumnInfo(name = "type")
    var type: Type
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0

    @ColumnInfo(name = "date")
    lateinit var date: Calendar

    @ColumnInfo(name = "sale-data")
    var saleData = HashMap<String, Int>().withDefault { 0 } // <Menu Item Name, # of sold>

    @ColumnInfo(name = "subtotal")
    var subTotal: Float = 0f

    @ColumnInfo(name = "tax")
    var tax: Float = 0f

    @ColumnInfo(name = "order-count")
    var orderCount: Int = 0

    private fun addSaleData(string: String, quantity: Int = 1) {
        if (saleData.containsKey(string)) {
            saleData[string] = saleData.getValue(string) + quantity
        } else {
            saleData[string] = quantity
        }
    }

    private fun addSaleData(menuItem: MenuItem, quantity: Int = 1) {
        addSaleData(menuItem.name, quantity)
    }

    private fun addSaleData(orderItem: OrderItem) {
        addSaleData(orderItem.item, orderItem.quantity)
    }

    fun addSaleData(order: Order) {
        order.orderItems.forEach {
            addSaleData(it)
        }
        subTotal += order.subtotalAmount
        tax += order.subtotalAmount * order.taxRate
        orderCount++
    }

    private fun removeSaleData(string: String, quantity: Int = 1) {
        if (saleData.containsKey(string)) {
            val v = saleData.getValue(string)
            if (v > quantity) {
                saleData[string] = v - quantity

            } else if (v == quantity) {
                saleData.remove(string)
            }
        }
    }

    private fun removeSaleData(menuItem: MenuItem, quantity: Int = 1) {
        removeSaleData(menuItem.name, quantity)
    }

    private fun removeSaleData(orderItem: OrderItem) {
        removeSaleData(orderItem.item, orderItem.quantity)
    }

    fun removeSaleData(order: Order) {
        order.orderItems.forEach {
            removeSaleData(it)
        }
        subTotal -= order.subtotalAmount
        tax -= order.subtotalAmount * order.taxRate
        orderCount--
    }

    companion object {
        fun newInstance(type: Type, orderCreatedTime: Calendar): SaleSummary =
            SaleSummary(type).apply {
                date =  when (type) {
                    Type.TODAY -> { getDayStart(orderCreatedTime) }
                    Type.THIS_WEEK -> { getWeekStart(orderCreatedTime) }
                    Type.THIS_MONTH -> { getMonthStart(orderCreatedTime) }
                }
            }
    }
}