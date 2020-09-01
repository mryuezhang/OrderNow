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