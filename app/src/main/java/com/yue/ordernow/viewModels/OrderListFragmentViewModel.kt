package com.yue.ordernow.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yue.ordernow.data.Order
import com.yue.ordernow.data.OrderRepository
import kotlinx.coroutines.launch

abstract class OrderListFragmentViewModel(private val orderRepository: OrderRepository) :
    ViewModel() {
    abstract val orders: LiveData<List<Order>>

    fun updateOrder(order: Order) {
        viewModelScope.launch {
            orderRepository.updateOrder(order)
        }
    }
}