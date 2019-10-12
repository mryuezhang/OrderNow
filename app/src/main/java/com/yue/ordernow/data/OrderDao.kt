package com.yue.ordernow.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OrderDao {

    @Query("SELECT * FROM `orders` ORDER BY `time-created`")
    fun getAllOrders(): LiveData<List<Order>>

    @Insert
    suspend fun insertOrder(order: Order): Long
}