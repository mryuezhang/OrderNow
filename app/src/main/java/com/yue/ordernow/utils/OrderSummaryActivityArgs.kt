package com.yue.ordernow.utils

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.yue.ordernow.activities.OrderSummaryActivity
import com.yue.ordernow.data.OrderItem

data class OrderSummaryActivityArgs(
    val orderItems: ArrayList<OrderItem>
) : ActivityArgs {

    override fun intent(context: AppCompatActivity): Intent =
        Intent(context, OrderSummaryActivity::class.java)
            .apply {
                putParcelableArrayListExtra(ARG_ORDERS, orderItems)
            }

    companion object {
        const val ARG_ORDERS = "order"

        fun create(intent: Intent): OrderSummaryActivityArgs =
            OrderSummaryActivityArgs(intent.getParcelableArrayListExtra(ARG_ORDERS))
    }
}