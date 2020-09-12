package com.yue.ordernow.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yue.ordernow.data.Order
import com.yue.ordernow.data.OrderItem
import com.yue.ordernow.data.OrderRepository
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel internal constructor(
    private val orderRepository: OrderRepository) : ViewModel() {
    val orderItems = ArrayList<OrderItem>()
    var subtotal = 0f
    var totalQuantity = 0
    var isTakeout = false
    var orderer = ""
    private var currentOrderNumber = 0
    private var lastOrderCreatedTime: Calendar? = null

    fun saveToDatabase(order: Order) {
        viewModelScope.launch {
            currentOrderNumber = order.orderNumber
            lastOrderCreatedTime = order.timeCreated
            orderRepository.insert(order)
        }
    }

    fun updateOrder(order: Order) {
        viewModelScope.launch {
            orderRepository.update(order)
        }
    }
}