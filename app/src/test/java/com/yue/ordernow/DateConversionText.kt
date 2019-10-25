package com.yue.ordernow

import com.yue.ordernow.utilities.*
import org.junit.Test
import java.util.*

class DateConversionText {

    @Test
    fun testGetDateStart() {

        val calendar = Calendar.getInstance()
        val dayStart = getDayStart(calendar)
        val dayEnd = getDayEnd(calendar)

        println(calendar.time.toString())
        println(dayStart.time.toString())
        println(dayEnd.time.toString())

        println(calendar.get(Calendar.MILLISECOND))
        println(dayStart.get(Calendar.MILLISECOND))
        println(dayEnd.get(Calendar.MILLISECOND))

        println(dayStart.timeInMillis)
        println(dayEnd.timeInMillis)
        println(getDayStartAndEnd(calendar).toString())


        println(getWeekStart(calendar).time.toString())
        println(getWeekStart(calendar.apply {
            set(Calendar.DAY_OF_WEEK, 5)
        }).time.toString())
        println(getWeekEnd(calendar).time.toString())
        println(getMonthStart(calendar).time.toString())
        println(getMonthEnd(calendar).time.toString())
        println(getYearStart(calendar).time.toString())
        println(getYearEnd(calendar).time.toString())
        println(getYearEnd(calendar).time.toString())
    }
}