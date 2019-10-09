package com.yue.ordernow.utils

import android.content.Context
import com.yue.ordernow.data.AppDatabase
import com.yue.ordernow.data.MenuItemRepository
import com.yue.ordernow.viewModels.RestaurantMenuViewModelFactory

object InjectorUtils {

    private fun getMenuItemRepository(context: Context): MenuItemRepository {
        return MenuItemRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext).menuItemDao()
        )
    }

    fun provideRestaurantMenuViewModelFactory(context: Context): RestaurantMenuViewModelFactory {
        val repository = getMenuItemRepository(context)
        return RestaurantMenuViewModelFactory(repository)
    }

}