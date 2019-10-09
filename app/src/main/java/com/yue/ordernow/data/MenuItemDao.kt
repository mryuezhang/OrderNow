package com.yue.ordernow.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yue.ordernow.utils.APPETIZER
import com.yue.ordernow.utils.BREAKFAST
import com.yue.ordernow.utils.DRINK
import com.yue.ordernow.utils.MAIN

@Dao
interface MenuItemDao {

    @Query("SELECT * FROM `menu-items` ORDER BY name")
    fun getItems(): LiveData<List<MenuItem>>

    @Query("SELECT * FROM `menu-items` WHERE category ='$MAIN' ORDER BY name")
    fun getMains(): LiveData<List<MenuItem>>

    @Query("SELECT * FROM `menu-items` WHERE category = '$APPETIZER' ORDER BY name")
    fun getAppetizers(): LiveData<List<MenuItem>>

    @Query("SELECT * FROM `menu-items` WHERE category = '$DRINK' ORDER BY name")
    fun getDrinks(): LiveData<List<MenuItem>>

    @Query("SELECT * FROM `menu-items` WHERE category = '$BREAKFAST' ORDER BY name")
    fun getBreakfasts(): LiveData<List<MenuItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(plants: List<MenuItem>)
}