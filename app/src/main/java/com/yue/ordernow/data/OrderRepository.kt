package com.yue.ordernow.data

import com.yue.ordernow.utilities.*
import java.util.*

class OrderRepository private constructor(private val orderDao: OrderDao) {

    fun getAllOrders() = orderDao.getAllOrders()

    fun getDailyOrders(calendar: Calendar) =
        orderDao.getOrdersBetween(
            getDayStart(calendar).timeInMillis,
            getDayEnd(calendar).timeInMillis
        )

    fun getDailyOrders(calendar: Calendar, searchText: String) = if (searchText == "") {
        orderDao.getOrdersBetween(
            getDayStart(calendar).timeInMillis,
            getDayEnd(calendar).timeInMillis
        )
    } else {
        orderDao.getOrdersBetweenBySearchText(
            "%$searchText%",
            getDayStart(calendar).timeInMillis,
            getDayEnd(calendar).timeInMillis
        )
    }


    fun getDailyUnPaidOrders(calendar: Calendar, searchText: String) = if (searchText == "") {
        orderDao.getUnPaidOrdersBetween(
            getDayStart(calendar).timeInMillis,
            getDayEnd(calendar).timeInMillis
        )
    } else {
        orderDao.getUnPaidOrdersBetweenBySearchText(
            "%$searchText%",
            getDayStart(calendar).timeInMillis,
            getDayEnd(calendar).timeInMillis
        )
    }

    fun getWeeklyOrders(calendar: Calendar) =
        orderDao.getOrdersBetween(
            getWeekStart(calendar).timeInMillis,
            getWeekEnd(calendar).timeInMillis
        )

    fun getWeeklyOrders(calendar: Calendar, searchText: String) = if (searchText == "") {
        orderDao.getOrdersBetween(
            getWeekStart(calendar).timeInMillis,
            getWeekEnd(calendar).timeInMillis
        )
    } else {
        orderDao.getOrdersBetweenBySearchText(
            "%$searchText%",
            getWeekStart(calendar).timeInMillis,
            getWeekEnd(calendar).timeInMillis
        )
    }

    fun getWeeklyUnPaidOrders(calendar: Calendar, searchText: String) = if (searchText == "") {
        orderDao.getUnPaidOrdersBetween(
            getWeekStart(calendar).timeInMillis,
            getWeekEnd(calendar).timeInMillis
        )
    } else {
        orderDao.getUnPaidOrdersBetweenBySearchText(
            "%$searchText%",
            getWeekStart(calendar).timeInMillis,
            getWeekEnd(calendar).timeInMillis
        )
    }

    fun getMonthlyOrders(calendar: Calendar) =
        orderDao.getOrdersBetween(
            getMonthStart(calendar).timeInMillis,
            getMonthEnd(calendar).timeInMillis
        )

    fun getMonthlyOrdersWithExtraDays(calendar: Calendar) =
        if (getWeekStart(calendar).get(Calendar.MONTH) != calendar.get(Calendar.MONTH)) {
            // If the current week contains days from 2 different months,
            // the result should include these days from the current week that are not from the
            // current month
            orderDao.getOrdersBetween(
                getWeekStart(calendar).timeInMillis,
                getMonthEnd(calendar).timeInMillis
            )
        } else {
            orderDao.getOrdersBetween(
                getMonthStart(calendar).timeInMillis,
                getMonthEnd(calendar).timeInMillis
            )
        }

    fun getMonthlyOrders(calendar: Calendar, searchText: String) = if (searchText == "") {
        orderDao.getOrdersBetween(
            getMonthStart(calendar).timeInMillis,
            getMonthEnd(calendar).timeInMillis
        )
    } else {
        orderDao.getOrdersBetweenBySearchText(
            "%$searchText%",
            getMonthStart(calendar).timeInMillis,
            getMonthEnd(calendar).timeInMillis
        )
    }

    fun getMonthlyUnpaidOrders(calendar: Calendar, searchText: String) = if (searchText == "") {
        orderDao.getUnPaidOrdersBetween(
            getMonthStart(calendar).timeInMillis,
            getMonthEnd(calendar).timeInMillis
        )
    } else {
        orderDao.getUnPaidOrdersBetweenBySearchText(
            "%$searchText%",
            getMonthStart(calendar).timeInMillis,
            getMonthEnd(calendar).timeInMillis
        )
    }

    fun getYearlyOrders(calendar: Calendar) =
        orderDao.getOrdersBetween(
            getYearStart(calendar).timeInMillis,
            getYearEnd(calendar).timeInMillis
        )

    fun getYearlyUnpaidOrders(calendar: Calendar) =
        orderDao.getUnPaidOrdersBetween(
            getYearStart(calendar).timeInMillis,
            getYearEnd(calendar).timeInMillis
        )

    fun getLastOrders(num: Int, searchText: String) = if (searchText == "") {
        orderDao.getOrders(num)
    } else {
        orderDao.getOrdersBySearchText("%$searchText%", num)
    }

    fun getLastUnpaidOrders(num: Int, searchText: String) = if (searchText == "") {
        orderDao.getUnpaidOrders(num)
    } else {
        orderDao.getUnpaidOrdersBySearchText("%$searchText%", num)
    }

    fun getInvalidOrders(searchText: String) = if (searchText == "") {
        orderDao.getInvalidOrders()
    } else {
        orderDao.getInvalidOrdersBySearchText("%$searchText%")
    }

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

        fun getInstance(orderDao: OrderDao) =
            instance ?: synchronized(this) {
                instance ?: OrderRepository(orderDao).also { instance = it }
            }
    }
}