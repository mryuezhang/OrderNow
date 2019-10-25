package com.yue.ordernow.utilities

import android.text.InputFilter
import android.text.Spanned
import java.util.*
import java.util.regex.Pattern

fun currencyFormat(rawInput: Float): String = String.format("$%.2f", rawInput)

class CurrencyFormatInputFilter : InputFilter {

    private val currencyPattern = Pattern.compile("(0|[1-9]+[0-9]*)?(\\.[0-9]{0,2})?")

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {

        val result = (dest.subSequence(0, dstart).toString()
                + source.toString()
                + dest.subSequence(dend, dest.length))

        val matcher = currencyPattern.matcher(result)

        return if (!matcher.matches()) "$" + dest.subSequence(dstart, dend) else null

    }
}


fun getDayStart(calendar: Calendar): Calendar =
    (calendar.clone() as Calendar).apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

fun getDayEnd(calendar: Calendar): Calendar =
    (calendar.clone() as Calendar).apply {
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }

fun getWeekStart(calendar: Calendar): Calendar =
    getDayStart(calendar).apply {
        set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
    }

fun getWeekEnd(calendar: Calendar): Calendar =
    getDayEnd(calendar).apply {
        set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        timeInMillis += 6 * DAY_IN_MILLISECONDS
    }

fun getMonthStart(calendar: Calendar): Calendar =
    getDayStart(calendar).apply {
        set(Calendar.DAY_OF_MONTH, 1)
    }

fun getMonthEnd(calendar: Calendar): Calendar =
    getMonthStart(calendar).apply {
        set(Calendar.MONTH, this.get(Calendar.MONTH) + 1)
        timeInMillis -= 1
    }

fun getYearStart(calendar: Calendar): Calendar =
    getDayStart(calendar).apply {
        set(Calendar.DAY_OF_YEAR, 1)
    }

fun getYearEnd(calendar: Calendar): Calendar =
    getYearStart(calendar).apply {
        set(Calendar.YEAR, this.get(Calendar.YEAR) + 1)
        timeInMillis -= 1
    }

const val DAY_IN_MILLISECONDS: Long = 86400000

fun getDayStartAndEnd(calendar: Calendar): Pair<Long, Long> {
    val dayStart = (calendar.clone() as Calendar).apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return Pair(dayStart.timeInMillis, dayStart.timeInMillis + DAY_IN_MILLISECONDS - 1)
}