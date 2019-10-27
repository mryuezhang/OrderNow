package com.yue.ordernow.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.yue.ordernow.R
import com.yue.ordernow.data.Report
import com.yue.ordernow.databinding.ActivityReportDetailBinding
import com.yue.ordernow.utilities.InjectorUtils
import com.yue.ordernow.viewModels.ReportDetailViewModel


const val REPORT = "report"

class ReportDetailActivity : AppCompatActivity() {

    val viewModel: ReportDetailViewModel by viewModels {
        InjectorUtils.provideReportDetailViewModelFactory(intent.getParcelableExtra(REPORT) as Report)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityReportDetailBinding =
            setContentView(this, R.layout.activity_report_detail)
    }
}