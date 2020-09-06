package com.yue.ordernow.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class Converter {

    private val gson = Gson()

    @TypeConverter
    fun calendarToLong(calendar: Calendar): Long = calendar.timeInMillis

    @TypeConverter
    fun longToCalendar(value: Long): Calendar =
        Calendar.getInstance().apply { timeInMillis = value }

    @TypeConverter
    fun orderItemArrayListToString(orderItems: ArrayList<OrderItem>): String =
        gson.toJson(orderItems)

    @TypeConverter
    fun stringToOrderItemArrayList(string: String): ArrayList<OrderItem> =
        gson.fromJson(string, object : TypeToken<ArrayList<OrderItem>>() {}.type)

    @TypeConverter
    fun reportTypeToInt(type: Report.Type): Int = type.value

    @TypeConverter
    fun intToReportType(int: Int): Report.Type = Report.Type.fromInt(int)

    @TypeConverter
    fun longListToString(list: ArrayList<Long>): String =
        gson.toJson(list)

    @TypeConverter
    fun stringToLongList(string: String): ArrayList<Long> =
        gson.fromJson(string, object : TypeToken<ArrayList<Long>>() {}.type)

    @TypeConverter
    fun saleDataToString(saleData: Map<String, Int>): String =
        gson.toJson(saleData)

    @TypeConverter
    fun stringToSaleData(string: String): Map<String, Int> =
        gson.fromJson(string, object : TypeToken<HashMap<String, Int>>() {}.type)
}