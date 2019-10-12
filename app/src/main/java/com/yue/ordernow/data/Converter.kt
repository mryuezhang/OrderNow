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
    fun menuItemArrayListToString(orderItems: ArrayList<OrderItem>): String =
        gson.toJson(orderItems)

    @TypeConverter
    fun stringToMenuItemArrayList(string: String): ArrayList<OrderItem> =
        gson.fromJson(string, object : TypeToken<ArrayList<OrderItem>>() {}.type)

}