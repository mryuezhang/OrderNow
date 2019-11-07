package com.yue.ordernow.data

import com.yue.ordernow.utilities.currencyFormat

data class Report(val type: Type, var quantity: Int, var amount: Float) {

    val orders = ArrayList<Order>()

    fun getFormattedAmount(): String = currencyFormat(amount)

    enum class Type(val value: Byte) {
        TODAY(0), THIS_WEEK(1), THIS_MONTH(2);

        companion object {
            fun fromByte(value: Byte) = values().first { it.value == value }
        }
    }
}