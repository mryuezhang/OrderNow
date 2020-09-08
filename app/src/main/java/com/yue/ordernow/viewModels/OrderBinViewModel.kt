package com.yue.ordernow.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.yue.ordernow.data.Order
import com.yue.ordernow.data.OrderRepository

class OrderBinViewModel(orderRepository: OrderRepository):
    AbstractOrderListFragmentViewModel() {

    override val orders: LiveData<List<Order>> = searchText.switchMap {
        orderRepository.getInvalidOrders(it)
    }
}