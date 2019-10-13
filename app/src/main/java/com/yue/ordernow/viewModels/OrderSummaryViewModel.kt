package com.yue.ordernow.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yue.ordernow.data.Order
import com.yue.ordernow.data.OrderRepository
import kotlinx.coroutines.launch

class OrderSummaryViewModel internal constructor(
    private val orderRepository: OrderRepository
) :
    ViewModel() {

    fun saveToDatabase(order: Order) {
        viewModelScope.launch {
            orderRepository.insertOrder(order)
        }
    }
}