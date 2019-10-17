package com.yue.ordernow.utils

import android.content.Context
import com.yue.ordernow.data.AppDatabase
import com.yue.ordernow.data.MenuItemRepository
import com.yue.ordernow.data.OrderRepository
import com.yue.ordernow.viewModels.MainViewModelFactory
import com.yue.ordernow.viewModels.MenuOptionsViewModelFactory
import com.yue.ordernow.viewModels.OrderHistoryViewModelFactory

object InjectorUtils {

    private fun getMenuItemRepository(context: Context): MenuItemRepository =
        MenuItemRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext).menuItemDao()
        )

    private fun getOrderRepository(context: Context): OrderRepository =
        OrderRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext).orderDao()
        )

    fun provideMainViewModelFactory(context: Context): MainViewModelFactory = MainViewModelFactory(
        getOrderRepository(context)
    )

    fun provideMenuOptionsViewModelFactory(
        context: Context,
        category: String
    ): MenuOptionsViewModelFactory =
        MenuOptionsViewModelFactory(getMenuItemRepository(context), category)

    fun provideOrderHistoryViewModelFactory(context: Context): OrderHistoryViewModelFactory =
        OrderHistoryViewModelFactory(getOrderRepository(context))
}