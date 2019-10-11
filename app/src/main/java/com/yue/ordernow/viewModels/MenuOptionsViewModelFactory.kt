package com.yue.ordernow.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yue.ordernow.data.MenuItemRepository

class MenuOptionsViewModelFactory(
    private val repository: MenuItemRepository,
    private val category: String
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        MenuOptionsViewModel(repository, category) as T
}