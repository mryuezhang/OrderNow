package com.yue.ordernow.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.yue.ordernow.data.Order
import com.yue.ordernow.data.OrderRepository
import com.yue.ordernow.data.Report
import kotlinx.coroutines.launch
import java.util.*

class ReportDetailFragmentViewModel internal constructor(
    private val orderRepository: OrderRepository,
    reportType: Report.Type,
    requestedTime: Calendar
) :
    ViewModel() {

    private val queryType = MutableLiveData<Int>(ALL)

    val orders = when (reportType) {
        Report.Type.TODAY -> {
            queryType.switchMap {
                if (it == ALL) orderRepository.getDailyOrders(requestedTime)
                else orderRepository.getDailyUnPaidOrders(requestedTime)
            }
        }
        Report.Type.THIS_WEEK -> {
            queryType.switchMap {
                if (it == ALL) orderRepository.getWeeklyOrders(requestedTime)
                else orderRepository.getWeeklyUnPaidOrders(requestedTime)
            }
        }
        Report.Type.THIS_MONTH -> {
            queryType.switchMap {
                if (it == ALL) orderRepository.getMonthlyOrders(requestedTime)
                else orderRepository.getMonthlyUnpaidOrders(requestedTime)
            }
        }
    }

    fun setQueryAllOrders() {
        queryType.value = ALL
    }

    fun setQueryUnPaidOrders() {
        queryType.value = UNPAID
    }

    fun isFiltered(): Boolean = queryType.value == UNPAID

    fun updateOrder(order: Order) {
        viewModelScope.launch {
            orderRepository.updateOrder(order)
        }
    }

    companion object {
        private const val ALL = 0
        private const val UNPAID = 1
    }
}