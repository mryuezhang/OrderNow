package com.yue.ordernow.viewModels

import androidx.lifecycle.ViewModel
import com.yue.ordernow.data.OrderRepository
import com.yue.ordernow.data.SaleSummaryRepository
import java.util.*

class DashboardViewModel internal constructor(
    orderRepository: OrderRepository,
    saleSummaryRepository: SaleSummaryRepository
) : ViewModel() {
    val now: Calendar = Calendar.getInstance()
    val orders = orderRepository.getAllOrders()
//    val dailyOrders = orderRepository.getDailyOrders(now)
//    val weeklyOrders = orderRepository.getWeeklyOrders(now)
    val monthlyOrders = orderRepository.getMonthlyOrdersWithExtraDays(now)
//    val yearlyOrders = orderRepository.getYearlyOrders(now)
    val saleSummaries = saleSummaryRepository.getAll()
    val dailySaleSummary = saleSummaryRepository.getDailySaleSummary(now)
    val weeklySaleSummary = saleSummaryRepository.getWeeklySaleSummary(now)
    val monthlySaleSummary = saleSummaryRepository.getMonthlySaleSummary(now)
}