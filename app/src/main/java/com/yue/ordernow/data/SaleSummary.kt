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

@Entity(tableName = "sale-summary")
data class SaleSummary(
    val type: Type
) {
    @PrimaryKey
    @ColumnInfo(name = "time")
    val time: Calendar = when (type) {
        Type.TODAY -> { getDayStart(Calendar.getInstance()) }
        Type.THIS_WEEK -> { getWeekStart(Calendar.getInstance()) }
        Type.THIS_MONTH -> { getMonthStart(Calendar.getInstance()) }
    }

    @ColumnInfo(name = "sale-data")
    val saleData = HashMap<String, Int>().withDefault { 0 } // <Menu Item Name, # of sold>

    fun addSaleData(menuItem: MenuItem, quantity: Int = 1) {
        saleData[menuItem.name] = saleData.getValue(menuItem.name) + quantity
    }

    fun addSaleData(orderItem: OrderItem) {
        addSaleData(orderItem.item, orderItem.quantity)
    }

    fun addSaleData(order: Order) {
        order.orderItems.forEach {
            addSaleData(it)
        }
    }


}