package com.yue.ordernow.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.yue.ordernow.data.Order
import com.yue.ordernow.data.OrderRepository

class RecentOrderViewModel(orderRepository: OrderRepository) :
    AbstractOrderListFragmentViewModel(orderRepository) {

    override val orders: LiveData<List<Order>> = queryType.switchMap {
        if (it == ALL) {
            searchText.switchMap { string ->
                if (string == "") {
                    orderRepository.getLastOrders(recentOrderCount)
                } else {
                    orderRepository.getLastOrdersBySearchText("%$string%", recentOrderCount)
                }
            }

        } else {
            searchText.switchMap { string ->
                if (string == "") {
                    orderRepository.getLastUnpaidOrders(recentOrderCount)
                } else {
                    orderRepository.getLastUnpaidOrdersBySearchText("%$string%", recentOrderCount)
                }
            }
        }
    }

    companion object {
        var recentOrderCount = 100
    }
}