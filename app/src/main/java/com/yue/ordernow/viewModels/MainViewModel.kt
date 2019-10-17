package com.yue.ordernow.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yue.ordernow.data.Order
import com.yue.ordernow.data.OrderItem
import com.yue.ordernow.data.OrderRepository
import kotlinx.coroutines.launch

class MainViewModel internal constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {
    val orderItems = ArrayList<OrderItem>()
    var subtotal = 0f
    var totalQuantity = 0

    fun saveToDatabase(order: Order) {
        viewModelScope.launch {
            orderRepository.insertOrder(order)
        }
    }
}