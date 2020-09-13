package com.yue.ordernow.viewModels

import androidx.lifecycle.switchMap
import com.yue.ordernow.data.OrderRepository
import com.yue.ordernow.data.Report
import com.yue.ordernow.fragments.ReportDetailFragmentArgs
import java.util.*

class ReportDetailViewModel internal constructor(
    orderRepository: OrderRepository,
    args: ReportDetailFragmentArgs
) : AbstractOrderListFragmentViewModel() {
    override val orders = when (args.StringArgReport.type) {
        Report.Type.TODAY -> {
            queryType.switchMap {
                if (it == ALL) {
                    searchText.switchMap { string ->
                        orderRepository.getDailyOrders(Calendar.getInstance().apply { timeInMillis = args.StringArgReport.timeStamp }, string)
                    }
                } else {
                    searchText.switchMap { string ->
                        orderRepository.getDailyUnPaidOrders(Calendar.getInstance().apply { timeInMillis = args.StringArgReport.timeStamp }, string)
                    }
                }
            }
        }
        Report.Type.THIS_WEEK -> {
            queryType.switchMap {
                if (it == ALL) {
                    searchText.switchMap { string ->
                        orderRepository.getWeeklyOrders(Calendar.getInstance().apply { timeInMillis = args.StringArgReport.timeStamp }, string)
                    }
                } else {
                    searchText.switchMap { string ->
                        orderRepository.getWeeklyUnPaidOrders(Calendar.getInstance().apply { timeInMillis = args.StringArgReport.timeStamp }, string)
                    }
                }
            }
        }
        Report.Type.THIS_MONTH -> {
            queryType.switchMap {
                if (it == ALL) {
                    searchText.switchMap { string ->
                        orderRepository.getMonthlyOrders(Calendar.getInstance().apply { timeInMillis = args.StringArgReport.timeStamp }, string)
                    }
                } else {
                    searchText.switchMap { string ->
                        orderRepository.getMonthlyUnpaidOrders(Calendar.getInstance().apply { timeInMillis = args.StringArgReport.timeStamp }, string)
                    }
                }
            }
        }
    }

    var takeOutCount = args.StringArgReport.takeOutCount
    var diningInCount = args.StringArgReport.diningInCount
}