package com.yue.ordernow.utilities

import com.github.mikephil.charting.formatter.ValueFormatter

class IntegerFormatter : ValueFormatter() {

    override fun getFormattedValue(value: Float): String =
        value.toInt().toString()
}

class TimeFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String =
        "${value.toInt()}:00"
}

class DayOfWeekFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String =
        when (value) {
            0f -> "Sun"
            1f -> "Mon"
            2f -> "Tue"
            3f -> "Wed"
            4f -> "Thu"
            5f -> "Fri"
            else -> "Sat"
        }
}

class DayOfMonthFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String =
        when (value) {
            0f -> "1st"
            1f -> "2nd"
            2f -> "3rd"
            else -> "${value.toInt() + 1}th"
        }

}