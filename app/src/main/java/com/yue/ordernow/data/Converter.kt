package com.yue.ordernow.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class Converter {

    private val gson = Gson()

    @TypeConverter
    fun calendarToDatestamp(calendar: Calendar): Long = calendar.timeInMillis

    @TypeConverter
    fun datestampToCalendar(value: Long): Calendar =
        Calendar.getInstance().apply { timeInMillis = value }

    @TypeConverter
    fun orderItemArrayListToString(orderItems: ArrayList<OrderItem>): String =
        gson.toJson(orderItems)

    @TypeConverter
    fun stringToOrderItemArrayList(string: String): ArrayList<OrderItem> =
        gson.fromJson(string, object : TypeToken<ArrayList<OrderItem>>() {}.type)

    @TypeConverter
    fun saleDataToString(saleData: Map<String, Int>): String =
        gson.toJson(saleData)

    @TypeConverter
    fun stringToSaleData(string: String): Map<String, Int> =
        gson.fromJson(string, object : TypeToken<Map<String, Int>>() {}.type)

}