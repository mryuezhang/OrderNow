package com.yue.ordernow.viewModels

import androidx.lifecycle.ViewModel
import com.yue.ordernow.data.OrderRepository
import java.util.*

class DashboardViewModel internal constructor(
    orderRepository: OrderRepository) : ViewModel() {
    internal val now = Calendar.getInstance()
    val monthlyOrders = orderRepository.getMonthlyOrdersWithExtraDays(now)
}