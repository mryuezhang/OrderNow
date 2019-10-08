package com.yue.ordernow.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MenuItemDao {

    @Query("SELECT * FROM `menu-items` ORDER BY name")
    fun getItems(): LiveData<List<MenuItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(plants: List<MenuItem>)
}