package com.yue.ordernow.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yue.ordernow.data.Order

abstract class AbstractOrderListFragmentViewModel :
    ViewModel() {
    protected val queryType = MutableLiveData(ALL)
    protected val searchText = MutableLiveData("")
    abstract val orders: LiveData<List<Order>>

    fun setQueryAllOrders() {
        queryType.value = ALL
    }

    fun setQueryUnPaidOrders() {
        queryType.value = UNPAID
    }

    fun setSearchText(string: String) {
        searchText.value = string
    }

    fun isFiltered(): Boolean = queryType.value == UNPAID

    companion object {
        internal const val ALL = 0
        internal const val UNPAID = 1
    }
}