package com.yue.ordernow.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yue.ordernow.data.Report

class ReportDetailViewModelFactory(
    private val reportType: Report.Type,
    private val takeoutCount: Int,
    private val diningInCount: Int,
    private val timeStamp: Long
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        ReportDetailViewModel(reportType, takeoutCount, diningInCount, timeStamp) as T
}