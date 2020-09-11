package com.yue.ordernow.viewModels

import androidx.lifecycle.ViewModel
import com.yue.ordernow.data.OrderRepository
import com.yue.ordernow.data.SaleSummaryRepository
import java.util.*

class DashboardViewModel internal constructor(
    orderRepository: OrderRepository,
    saleSummaryRepository: SaleSummaryRepository
) : ViewModel() {
    internal val now = Calendar.getInstance()
    val dailyOrders = orderRepository.getDailyOrders(now)
    val weeklyOrders = orderRepository.getWeeklyOrders(now)
    val monthlyOrders = orderRepository.getMonthlyOrdersWithExtraDays(now)
    val saleSummaries = saleSummaryRepository.getAllRelatedSaleSummaries(now)
}