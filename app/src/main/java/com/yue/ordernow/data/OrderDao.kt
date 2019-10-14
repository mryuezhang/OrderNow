package com.yue.ordernow.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface OrderDao {

    @Query("SELECT * FROM `orders` ORDER BY `time-created`")
    fun getAllOrders(): LiveData<List<Order>>

    @Query("SELECT * FROM `orders` ORDER BY `time-created` DESC LIMIT 1")
    fun getLastOrder(): LiveData<Order>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)

    @Query("DELETE FROM `orders`")
    suspend fun deleteAllOrders()
}