package com.yue.ordernow.data

import com.yue.ordernow.utilities.*
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

    suspend fun deleteAllOrders() {
        orderDao.deleteAllOrders()
    }

    suspend fun insertOrder(order: Order) {
        orderDao.insertOrder(order)
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