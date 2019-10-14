package com.yue.ordernow.viewModels

import androidx.lifecycle.ViewModel
import com.yue.ordernow.data.OrderItem

class MainViewModel internal constructor() : ViewModel() {
    val orderItems = ArrayList<OrderItem>()
}