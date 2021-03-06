package com.yue.ordernow.utilities

import android.content.Context
import com.yue.ordernow.data.AppDatabase
import com.yue.ordernow.data.MenuItemRepository
import com.yue.ordernow.data.OrderRepository
import com.yue.ordernow.fragments.ReportDetailFragmentArgs
import com.yue.ordernow.viewModels.*

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

    fun provideDashboardViewModelFactory(context: Context): DashboardViewModelFactory =
        DashboardViewModelFactory(getOrderRepository(context))

    fun provideReportDetailFragmentViewModelFactory(
        context: Context,
        args: ReportDetailFragmentArgs
    ): ReportDetailViewModelFactory =
        ReportDetailViewModelFactory(getOrderRepository(context), args)

    fun provideRecentOrderViewModelFactory(context: Context): RecentOrderViewModelFactory =
        RecentOrderViewModelFactory(getOrderRepository(context))

    fun provideOrderBinViewModelFactory(context: Context): OrderBinViewModelFactory =
        OrderBinViewModelFactory(getOrderRepository(context))
}