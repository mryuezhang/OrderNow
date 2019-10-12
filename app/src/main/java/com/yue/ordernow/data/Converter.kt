package com.yue.ordernow.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList

class Converter {

    private val gson = Gson()

    @TypeConverter
    fun calendarToDatestamp(calendar: Calendar): Long = calendar.timeInMillis

    @TypeConverter
    fun datestampToCalendar(value: Long): Calendar =
        Calendar.getInstance().apply { timeInMillis = value }

    @TypeConverter
    fun menuItemArrayListToString(menuItems: List<MenuItem>): String = gson.toJson(menuItems)

    @TypeConverter
    fun stringToMenuItemArrayList(string: String): ArrayList<MenuItem> =
        gson.fromJson(string, ArrayList<MenuItem>()::class.java)
}