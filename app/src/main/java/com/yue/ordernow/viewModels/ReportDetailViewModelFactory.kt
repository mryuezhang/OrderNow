package com.yue.ordernow.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yue.ordernow.data.OrderRepository
import com.yue.ordernow.data.SaleSummaryRepository
import com.yue.ordernow.fragments.ReportDetailFragmentArgs

class ReportDetailViewModelFactory(
    private val orderRepository: OrderRepository,
    private val saleSummaryRepository: SaleSummaryRepository,
    private val args: ReportDetailFragmentArgs
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        ReportDetailViewModel(orderRepository, saleSummaryRepository, args) as T
}