package com.yue.ordernow.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yue.ordernow.data.*
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel internal constructor(
    private val orderRepository: OrderRepository,
    private val saleSummaryRepository: SaleSummaryRepository
) : ViewModel() {
    val orderItems = ArrayList<OrderItem>()
    var subtotal = 0f
    var totalQuantity = 0
    var isTakeout = false
    var orderer = ""
    var dailySaleSummary = saleSummaryRepository.getDailySaleSummary(Calendar.getInstance())
    var weeklySaleSummary = saleSummaryRepository.getWeeklySaleSummary(Calendar.getInstance())
    var monthlySaleSummary = saleSummaryRepository.getMonthlySaleSummary(Calendar.getInstance())

    init {
        viewModelScope.launch {
            saleSummaryRepository.insert(SaleSummary.newInstance(Report.Type.TODAY, Calendar.getInstance()))
            saleSummaryRepository.insert(SaleSummary.newInstance(Report.Type.THIS_WEEK, Calendar.getInstance()))
            saleSummaryRepository.insert(SaleSummary.newInstance(Report.Type.THIS_MONTH, Calendar.getInstance()))
        }
    }

    fun saveToDatabase(order: Order) {
        viewModelScope.launch {
            orderRepository.insert(order)
        }
    }

    fun updateSaleSummary(saleSummary: SaleSummary?) {
        viewModelScope.launch {
            if (saleSummary != null) {
                saleSummaryRepository.update(saleSummary)
            } else {
                Log.i("FFF", "FFFFF")
            }

        }
    }
}