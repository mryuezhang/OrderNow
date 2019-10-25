package com.yue.ordernow.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.recyclerview.widget.DividerItemDecoration
import com.yue.ordernow.R
import com.yue.ordernow.adapters.OrderAdapter
import com.yue.ordernow.data.Report
import com.yue.ordernow.databinding.ActivityReportDetailBinding


const val REPORT = "report"

class ReportDetailActivity : AppCompatActivity() {

    private val report: Report by lazy {
        intent.getParcelableExtra(REPORT) as Report
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityReportDetailBinding =
            setContentView(this, R.layout.activity_report_detail)
        setSupportActionBar(binding.reportDetailToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val adapter = OrderAdapter(this)
        binding.orderList.adapter = adapter
        binding.orderList.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        adapter.submitList(report.orders)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }
}