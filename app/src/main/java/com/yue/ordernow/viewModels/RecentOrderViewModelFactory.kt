package com.yue.ordernow.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yue.ordernow.data.OrderRepository

class RecentOrderViewModelFactory(private val orderRepository: OrderRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        RecentOrderViewModel(orderRepository) as T
}