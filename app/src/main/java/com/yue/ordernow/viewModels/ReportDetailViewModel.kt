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
    AbstractOrderListFragmentViewModel(orderRepository) {

    override val orders = when (reportType) {
        Report.Type.TODAY -> {
            queryType.switchMap {
                if (it == ALL) {
                    searchText.switchMap { string ->
                        orderRepository.getDailyOrders(requestedTime, string)
                    }

                } else {
                    searchText.switchMap { string ->
                        orderRepository.getDailyUnPaidOrders(requestedTime, string)
                    }
                }
            }
        }
        Report.Type.THIS_WEEK -> {
            queryType.switchMap {
                if (it == ALL) {
                    searchText.switchMap { string ->
                        orderRepository.getWeeklyOrders(requestedTime, string)
                    }
                } else {
                    searchText.switchMap { string ->
                        orderRepository.getWeeklyUnPaidOrders(requestedTime, string)
                    }
                }
            }
        }
        Report.Type.THIS_MONTH -> {
            queryType.switchMap {
                if (it == ALL) {
                    searchText.switchMap { string ->
                        orderRepository.getMonthlyOrders(requestedTime, string)
                    }
                } else {
                    searchText.switchMap { string ->
                        orderRepository.getMonthlyUnpaidOrders(requestedTime, string)
                    }
                }
            }
        }
    }
}