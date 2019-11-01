package com.yue.ordernow.viewModels

import androidx.lifecycle.ViewModel
import com.yue.ordernow.data.Report

class ReportDetailViewModel internal constructor(
    val report: Report,
    val takeoutCount: Int,
    val diningInCount: Int
) : ViewModel()