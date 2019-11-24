package com.yue.ordernow.viewModels

import androidx.lifecycle.LiveData
import com.yue.ordernow.data.Order
import com.yue.ordernow.data.OrderRepository

class RecentOrderViewModel(orderRepository: OrderRepository) :
    OrderListFragmentViewModel(orderRepository) {

    override val orders: LiveData<List<Order>> = orderRepository.getAllOrders()
}