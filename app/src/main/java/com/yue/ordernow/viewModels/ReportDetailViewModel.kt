package com.yue.ordernow.viewModels

import androidx.lifecycle.switchMap
import com.yue.ordernow.data.OrderRepository
import com.yue.ordernow.data.Report
import java.util.*

class ReportDetailViewModel internal constructor(
    orderRepository: OrderRepository,
    reportType: Report.Type,
    requestedTime: Calendar
) :
    OrderListFragmentViewModel(orderRepository) {

    override val orders = when (reportType) {
        Report.Type.TODAY -> {
            queryType.switchMap {
                if (it == ALL) {
                    orderRepository.getDailyOrders(requestedTime)
                } else {
                    orderRepository.getDailyUnPaidOrders(requestedTime)
                }
            }
        }
        Report.Type.THIS_WEEK -> {
            queryType.switchMap {
                if (it == ALL) {
                    orderRepository.getWeeklyOrders(requestedTime)
                } else {
                    orderRepository.getWeeklyUnPaidOrders(requestedTime)
                }
            }
        }
        Report.Type.THIS_MONTH -> {
            queryType.switchMap {
                if (it == ALL) {
                    orderRepository.getMonthlyOrders(requestedTime)
                } else {
                    orderRepository.getMonthlyUnpaidOrders(requestedTime)
                }
            }
        }
    }
}