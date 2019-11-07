package com.yue.ordernow.viewModels

import androidx.lifecycle.ViewModel
import com.yue.ordernow.data.Report

class ReportDetailViewModel internal constructor(
    val reportType: Report.Type,
    val takeoutCount: Int,
    val diningInCount: Int,
    val timeStamp: Long
) : ViewModel()