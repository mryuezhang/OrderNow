package com.yue.ordernow.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yue.ordernow.data.Order
import com.yue.ordernow.data.OrderRepository
import kotlinx.coroutines.launch

abstract class OrderListFragmentViewModel(private val orderRepository: OrderRepository) :
    ViewModel() {
    protected val queryType = MutableLiveData<Int>(ALL)

    abstract val orders: LiveData<List<Order>>

    fun updateOrder(order: Order) {
        viewModelScope.launch {
            orderRepository.updateOrder(order)
        }
    }

    fun setQueryAllOrders() {
        queryType.value = ALL
    }

    fun setQueryUnPaidOrders() {
        queryType.value = UNPAID
    }

    fun isFiltered(): Boolean = queryType.value == UNPAID

    companion object {
        internal const val ALL = 0
        internal const val UNPAID = 1
    }
}