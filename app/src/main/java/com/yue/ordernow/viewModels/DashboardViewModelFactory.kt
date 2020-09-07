package com.yue.ordernow.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yue.ordernow.data.OrderRepository
import com.yue.ordernow.data.SaleSummaryRepository

class DashboardViewModelFactory(
    private val repository: OrderRepository,
    private val saleSummaryRepository: SaleSummaryRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        DashboardViewModel(repository, saleSummaryRepository) as T
}