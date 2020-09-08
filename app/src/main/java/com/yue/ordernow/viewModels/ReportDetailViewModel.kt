package com.yue.ordernow.viewModels

import androidx.lifecycle.switchMap
import com.yue.ordernow.data.OrderRepository
import com.yue.ordernow.data.Report
import com.yue.ordernow.data.SaleSummaryRepository
import com.yue.ordernow.fragments.ReportDetailFragmentArgs
import java.util.*

class ReportDetailViewModel internal constructor(
    orderRepository: OrderRepository,
    saleSummaryRepository: SaleSummaryRepository,
    args: ReportDetailFragmentArgs
) : AbstractOrderListFragmentViewModel() {
    override val orders = when (Report.Type.fromInt(args.StringArgReportType)) {
        Report.Type.TODAY -> {
            queryType.switchMap {
                if (it == ALL) {
                    searchText.switchMap { string ->
                        orderRepository.getDailyOrders(Calendar.getInstance().apply { timeInMillis = args.StringArgTimeStamp }, string)
                    }

                } else {
                    searchText.switchMap { string ->
                        orderRepository.getDailyUnPaidOrders(Calendar.getInstance().apply { timeInMillis = args.StringArgTimeStamp }, string)
                    }
                }
            }
        }
        Report.Type.THIS_WEEK -> {
            queryType.switchMap {
                if (it == ALL) {
                    searchText.switchMap { string ->
                        orderRepository.getWeeklyOrders(Calendar.getInstance().apply { timeInMillis = args.StringArgTimeStamp }, string)
                    }
                } else {
                    searchText.switchMap { string ->
                        orderRepository.getWeeklyUnPaidOrders(Calendar.getInstance().apply { timeInMillis = args.StringArgTimeStamp }, string)
                    }
                }
            }
        }
        Report.Type.THIS_MONTH -> {
            queryType.switchMap {
                if (it == ALL) {
                    searchText.switchMap { string ->
                        orderRepository.getMonthlyOrders(Calendar.getInstance().apply { timeInMillis = args.StringArgTimeStamp }, string)
                    }
                } else {
                    searchText.switchMap { string ->
                        orderRepository.getMonthlyUnpaidOrders(Calendar.getInstance().apply { timeInMillis = args.StringArgTimeStamp }, string)
                    }
                }
            }
        }
    }

    val saleSummary =  when (Report.Type.fromInt(args.StringArgReportType)) {
        Report.Type.TODAY -> {
            saleSummaryRepository.getDailySaleSummary(Calendar.getInstance().apply { timeInMillis = args.StringArgTimeStamp })
        }
        Report.Type.THIS_WEEK -> {
            saleSummaryRepository.getWeeklySaleSummary(Calendar.getInstance().apply { timeInMillis = args.StringArgTimeStamp })
        }
        Report.Type.THIS_MONTH -> {
            saleSummaryRepository.getMonthlySaleSummary(Calendar.getInstance().apply { timeInMillis = args.StringArgTimeStamp })
        }
    }

    var takeOutCount = args.StringArgTakeoutCount
    var diningInCount = args.StringArgDiningInCount

}