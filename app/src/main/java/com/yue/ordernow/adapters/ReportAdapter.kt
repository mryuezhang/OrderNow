package com.yue.ordernow.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.yue.ordernow.R
import com.yue.ordernow.data.Report
import com.yue.ordernow.databinding.ListItemReportBinding
import com.yue.ordernow.utilities.*


class ReportAdapter(private val listener: ReportClickListener) :
    ListAdapter<Report, RecyclerView.ViewHolder>(ReportDiffCallback()) {

    interface ReportClickListener {
        fun onClick(report: Report)

        fun requestContext(): Context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ReportViewHolder(
            ListItemReportBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
            )
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val report = getItem(position)
        (holder as ReportViewHolder).bind(report)
    }

    override fun getItemId(position: Int): Long = position.toLong()

    private inner class ReportViewHolder(private val binding: ListItemReportBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.setOnClickListener {
                binding.report?.let { report ->
                    listener.onClick(report)
                }
            }
        }

        fun bind(item: Report) {
            binding.apply {
                report = item
                executePendingBindings()

                // Set text using string resource here instead of using data binding for translatability
                title.text = when (item.type) {
                    Report.Type.TODAY -> listener.requestContext().getString(R.string.today)
                    Report.Type.THIS_WEEK -> listener.requestContext().getString(R.string.this_week)
                    Report.Type.THIS_MONTH -> listener.requestContext().getString(R.string.this_month)
                }

                if (item.quantity != 0) {
                    initBarChart(this.barChart, item)
                    setBarChartData(this.barChart, item)
                } else {
                    // Disable clickability when there is no orders
                    this.root.isClickable = false

                    // Hide chart when there is no orders
                    this.charts.visibility = View.GONE
                }
            }
        }

        private fun initBarChart(barChart: BarChart, report: Report) {
            val textPrimaryColor =
                listener.requestContext().getThemeColor(android.R.attr.textColorPrimary)
            barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            barChart.xAxis.setDrawGridLines(false)
            barChart.xAxis.granularity = 1f
            barChart.xAxis.textColor = textPrimaryColor
            when (report.type) {
                Report.Type.TODAY -> {
                    barChart.xAxis.valueFormatter = TimeFormatter()
                    barChart.xAxis.setLabelCount(8, false)
                }
                Report.Type.THIS_WEEK -> {
                    barChart.xAxis.valueFormatter = DayOfWeekFormatter()
                    barChart.xAxis.setLabelCount(7, false)
                }
                Report.Type.THIS_MONTH -> {
                    barChart.xAxis.valueFormatter = DayOfMonthFormatter()
                    barChart.xAxis.setLabelCount(12, false)
                }
            }

            barChart.axisLeft.setDrawGridLines(false)
//            barChart.axisRight.setDrawGridLines(false)
            barChart.axisLeft.granularity = 1f
            barChart.axisRight.granularity = 1f
            barChart.axisLeft.axisMinimum = 0f
            barChart.axisRight.axisMinimum = 0f
            barChart.axisLeft.textColor = textPrimaryColor
            barChart.axisRight.textColor = textPrimaryColor

            barChart.setFitBars(true)
            barChart.description.isEnabled = false
            barChart.animateY(800)
            barChart.legend.isEnabled = false
            barChart.description.isEnabled = false
        }

        private fun setBarChartData(barChart: BarChart, report: Report) {
            val dataSet = BarDataSet(report.barEntries, "").apply {
                setDrawIcons(false)
                this.valueFormatter = ValueOverBarFormatter()
                this.color = ContextCompat.getColor(listener.requestContext(), R.color.colorPrimary)
            }
            barChart.data = BarData(dataSet)
        }
    }

    private class ReportDiffCallback : DiffUtil.ItemCallback<Report>() {

        override fun areItemsTheSame(oldItem: Report, newItem: Report): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Report, newItem: Report): Boolean =
            oldItem == newItem
    }
}