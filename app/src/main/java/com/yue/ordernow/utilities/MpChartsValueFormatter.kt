package com.yue.ordernow.utilities

import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat

class IntegerFormatter : ValueFormatter() {

    override fun getFormattedValue(value: Float): String =
        value.toInt().toString()
}

class ValueOverBarFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String =
        if (value != 0f) {
            value.toInt().toString()
        } else {
            ""
        }
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

class PercentFormatter(private val pieChart: PieChart) : ValueFormatter() {

    var mFormat: DecimalFormat = DecimalFormat("###,###,##0.0")

    override fun getFormattedValue(value: Float): String = if (value != 0f) {
        mFormat.format(value.toDouble()) + " %"
    } else {
        ""
    }

    override fun getPieLabel(value: Float, pieEntry: PieEntry?): String {
        return if (pieChart.isUsePercentValuesEnabled) {
            getFormattedValue(value)
        } else {
            mFormat.format(value.toDouble())
        }
    }

}