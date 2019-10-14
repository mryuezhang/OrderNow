package com.yue.ordernow.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yue.ordernow.data.OrderRepository
import kotlinx.coroutines.launch

class OrderHistoryViewModel internal constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    val orders = orderRepository.getAllOrders()

    fun deleteAllOrders() {
        viewModelScope.launch {
            orderRepository.deleteAllOrders()
        }
    }
}