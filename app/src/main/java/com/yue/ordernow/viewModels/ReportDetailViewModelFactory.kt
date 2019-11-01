package com.yue.ordernow.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yue.ordernow.data.Report

class ReportDetailViewModelFactory(
    private val report: Report,
    private val takeoutCount: Int,
    private val diningInCount: Int
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        ReportDetailViewModel(report, takeoutCount, diningInCount) as T
}