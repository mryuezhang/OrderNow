package com.yue.ordernow.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.yue.ordernow.data.MenuItem
import com.yue.ordernow.data.MenuItemRepository
import com.yue.ordernow.utils.APPETIZER
import com.yue.ordernow.utils.BREAKFAST
import com.yue.ordernow.utils.MAIN

class MenuOptionsViewModel internal constructor(
    menuItemRepository: MenuItemRepository,
    category: String
) :
    ViewModel() {

    val menuItems: LiveData<List<MenuItem>> = when (category) {
        MAIN -> menuItemRepository.getMains()
        APPETIZER -> menuItemRepository.getAppetizers()
        BREAKFAST -> menuItemRepository.getBreakfasts()
        else -> menuItemRepository.getDrinks()
    }
}