package com.yue.ordernow.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.yue.ordernow.data.Order
import com.yue.ordernow.data.OrderRepository

class RecentOrderViewModel(orderRepository: OrderRepository) :
    OrderListFragmentViewModel(orderRepository) {

    override val orders: LiveData<List<Order>> = queryType.switchMap {
        if (it == ALL) {
            orderRepository.getLastOrders(recentOrderCount)
        } else {
            orderRepository.getLastUnpaidOrders(recentOrderCount)
        }
    }

    companion object {
        var recentOrderCount = 100
    }
}