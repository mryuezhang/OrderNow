package com.yue.ordernow.viewModels

import androidx.lifecycle.ViewModel
import com.yue.ordernow.data.MenuItemRepository

class RestaurantMenuViewModel internal constructor(val menuItemRepository: MenuItemRepository) :
    ViewModel()