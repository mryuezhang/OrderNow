package com.yue.ordernow.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yue.ordernow.data.OrderRepository
import com.yue.ordernow.data.Report
import java.util.*

class ReportDetailFragmentViewModelFactory(
    private val orderRepository: OrderRepository,
    private val reportType: Report.Type,
    private val requestedTime: Calendar
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        ReportDetailFragmentViewModel(orderRepository, reportType, requestedTime) as T
}