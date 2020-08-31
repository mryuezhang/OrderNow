package com.yue.ordernow.data

import com.yue.ordernow.utilities.getDayEnd
import com.yue.ordernow.utilities.getDayStart
import com.yue.ordernow.utilities.getMonthEnd
import com.yue.ordernow.utilities.getMonthStart
import com.yue.ordernow.utilities.getWeekEnd
import com.yue.ordernow.utilities.getWeekStart
import com.yue.ordernow.utilities.getYearEnd
import com.yue.ordernow.utilities.getYearStart
import java.util.*

class OrderRepository private constructor(private val orderDao: OrderDao) {

    fun getAllOrders() = orderDao.getAllOrders()

    fun getDailyOrders(calendar: Calendar) = orderDao.getOrdersBetween(
        getDayStart(calendar).timeInMillis,
        getDayEnd(calendar).timeInMillis
    )

    fun getDailyUnPaidOrders(calendar: Calendar) = orderDao.getUnPaidOrdersBetween(
        getDayStart(calendar).timeInMillis,
        getDayEnd(calendar).timeInMillis
    )

    fun getWeeklyOrders(calendar: Calendar) = orderDao.getOrdersBetween(
        getWeekStart(calendar).timeInMillis,
        getWeekEnd(calendar).timeInMillis
    )

    fun getWeeklyUnPaidOrders(calendar: Calendar) = orderDao.getUnPaidOrdersBetween(
        getWeekStart(calendar).timeInMillis,
        getWeekEnd(calendar).timeInMillis
    )

    fun getMonthlyOrders(calendar: Calendar) = orderDao.getOrdersBetween(
        getMonthStart(calendar).timeInMillis,
        getMonthEnd(calendar).timeInMillis
    )

    fun getMonthlyUnpaidOrders(calendar: Calendar) = orderDao.getUnPaidOrdersBetween(
        getMonthStart(calendar).timeInMillis,
        getMonthEnd(calendar).timeInMillis
    )

    fun getYearlyOrders(calendar: Calendar) = orderDao.getOrdersBetween(
        getYearStart(calendar).timeInMillis,
        getYearEnd(calendar).timeInMillis
    )

    fun getYearlyUnpaidOrders(calendar: Calendar) = orderDao.getUnPaidOrdersBetween(
        getYearStart(calendar).timeInMillis,
        getYearEnd(calendar).timeInMillis
    )

    fun getLastOrders(num: Int) = orderDao.getOrders(num)

    fun getLastUnpaidOrders(num: Int) = orderDao.getUnpaidOrders(num)

    fun getLastOrdersBySearchText(searchText: String, num: Int) =
        orderDao.getOrdersBySearchText(searchText, num)

    fun getLastUnpaidOrdersBySearchText(searchText: String, num: Int) =
        orderDao.getUnpaidOrdersBySearchText(searchText, num)

    suspend fun deleteAllOrders() {
        orderDao.deleteAllOrders()
    }

    suspend fun insertOrder(order: Order) {
        orderDao.insertOrder(order)
    }

    suspend fun updateOrder(order: Order) {
        orderDao.updateOrder(order)
    }

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: OrderRepository? = null

        fun getInstance(gardenPlantingDao: OrderDao) =
            instance ?: synchronized(this) {
                instance ?: OrderRepository(gardenPlantingDao).also { instance = it }
            }
    }
}