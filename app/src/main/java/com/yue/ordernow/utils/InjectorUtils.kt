package com.yue.ordernow.utils

import android.content.Context
import com.yue.ordernow.data.AppDatabase
import com.yue.ordernow.data.MenuItemRepository
import com.yue.ordernow.viewModels.MenuOptionsViewModelFactory

object InjectorUtils {

    private fun getMenuItemRepository(context: Context): MenuItemRepository {
        return MenuItemRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext).menuItemDao()
        )
    }

    fun provideMenuOptionsViewModelFactory(
        context: Context,
        category: String
    ): MenuOptionsViewModelFactory {
        val repository = getMenuItemRepository(context)
        return MenuOptionsViewModelFactory(repository, category)
    }

}