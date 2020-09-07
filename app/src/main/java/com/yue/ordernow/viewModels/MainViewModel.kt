package com.yue.ordernow.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yue.ordernow.data.*
import kotlinx.coroutines.launch

class MainViewModel internal constructor(
    private val orderRepository: OrderRepository,
    private val saleSummaryRepository: SaleSummaryRepository
) : ViewModel() {
    val orderItems = ArrayList<OrderItem>()
    var subtotal = 0f
    var totalQuantity = 0
    var isTakeout = false
    var orderer = ""

    fun saveToDatabase(order: Order) {
        viewModelScope.launch {
            var dailySaleSummary = saleSummaryRepository.getDailySaleSummary(order.timeCreated).value
            if (dailySaleSummary == null) {
                dailySaleSummary = SaleSummary.newInstance(Report.Type.TODAY, order.timeCreated)
                dailySaleSummary.addSaleData(order)
                saleSummaryRepository.insert(dailySaleSummary)
            } else {
                dailySaleSummary.addSaleData(order)
                saleSummaryRepository.update(dailySaleSummary)
            }

            var weeklySaleSummary = saleSummaryRepository.getWeeklySaleSummary(order.timeCreated).value
            if (weeklySaleSummary == null) {
                weeklySaleSummary = SaleSummary.newInstance(Report.Type.THIS_WEEK, order.timeCreated)
                weeklySaleSummary.addSaleData(order)
                saleSummaryRepository.insert(weeklySaleSummary)
            } else {
                weeklySaleSummary.addSaleData(order)
                saleSummaryRepository.update(weeklySaleSummary)
            }

            var monthlySaleSummary = saleSummaryRepository.getMonthlySaleSummary(order.timeCreated).value
            if (monthlySaleSummary == null) {
                monthlySaleSummary = SaleSummary.newInstance(Report.Type.THIS_MONTH, order.timeCreated)
                monthlySaleSummary.addSaleData(order)
                saleSummaryRepository.insert(monthlySaleSummary)
            } else {
                monthlySaleSummary.addSaleData(order)
                saleSummaryRepository.update(monthlySaleSummary)
            }

            orderRepository.insert(order)
        }
    }
}