package com.yue.ordernow.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface OrderDao {

    @Query("SELECT * FROM `orders` ORDER BY `time-created`")
    fun getAllOrders(): LiveData<List<Order>>

    @Query("SELECT * FROM `orders` ORDER BY `time-created` DESC LIMIT 1")
    fun getLastOrder(): LiveData<Order>

    @Query("SELECT * FROM `orders` WHERE `time-created` BETWEEN :start AND :end ORDER BY `time-created`")
    fun getOrdersBetween(start: Long, end: Long): LiveData<List<Order>>

    @Query("SELECT * FROM `orders` WHERE `time-created` BETWEEN :start AND :end AND `is-paid` == 0 ORDER BY `time-created`")
    fun getUnPaidOrdersBetween(start: Long, end: Long): LiveData<List<Order>>

    @Query("SELECT * FROM `orders` ORDER BY `time-created` DESC LIMIT :num")
    fun getLastOrders(num: Int): LiveData<List<Order>>

    @Query("SELECT * FROM (SELECT * FROM `orders` ORDER BY `time-created` DESC LIMIT :num) WHERE `is-paid` == 0")
    fun getLastUnpaidOrders(num: Int): LiveData<List<Order>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)

    @Update
    suspend fun updateOrder(order: Order)

    @Query("DELETE FROM `orders`")
    suspend fun deleteAllOrders()
}