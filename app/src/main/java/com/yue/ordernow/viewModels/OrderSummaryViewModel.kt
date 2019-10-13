package com.yue.ordernow.viewModels

import androidx.lifecycle.ViewModel
import com.yue.ordernow.data.OrderRepository

class OrderSummaryViewModel internal constructor(
    private val orderRepository: OrderRepository
) :
    ViewModel()