package com.yue.ordernow.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.yue.ordernow.data.Order
import com.yue.ordernow.data.OrderRepository

class RecentOrderViewModel(orderRepository: OrderRepository) :
    AbstractOrderListFragmentViewModel() {

    override val orders: LiveData<List<Order>> = queryType.switchMap {
        if (it == ALL) {
            searchText.switchMap { string ->
                orderRepository.getLastOrders(recentOrderCount, string)
            }
        } else {
            searchText.switchMap { string ->
                orderRepository.getLastUnpaidOrders(recentOrderCount, string)
            }
        }
    }

    companion object {
        var recentOrderCount = 100
    }
}