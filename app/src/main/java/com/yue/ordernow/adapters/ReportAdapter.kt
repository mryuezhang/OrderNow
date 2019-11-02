package com.yue.ordernow.adapters

import android.app.Activity
import android.content.Intent
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
import com.github.mikephil.charting.data.BarEntry
import com.yue.ordernow.R
import com.yue.ordernow.activities.ReportDetailActivity
import com.yue.ordernow.data.Report
import com.yue.ordernow.databinding.ListItemReportBinding
import com.yue.ordernow.utilities.*
import java.util.*
import kotlin.collections.ArrayList


class ReportAdapter(private val activity: Activity) :
    ListAdapter<Report, RecyclerView.ViewHolder>(ReportDiffCallback()) {

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

    private inner class ReportViewHolder(private val binding: ListItemReportBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var takeOutCount = 0
        private var diningInCount = 0

        init {
            binding.setOnClickListener {
                binding.report?.let { report ->
                    val intent = Intent(activity, ReportDetailActivity::class.java)
                    intent.putExtra(ReportDetailActivity.REPORT, report)
                    intent.putExtra(ReportDetailActivity.TAKEOUT_COUNT, takeOutCount)
                    intent.putExtra(ReportDetailActivity.DINING_IN_COUNT, diningInCount)
                    activity.startActivity(intent)

                    // Add slide animations
                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }
        }

        fun bind(item: Report) {
            binding.apply {
                report = item
                executePendingBindings()

                // Set text using string resource here instead of using data binding for translatability
                title.text = when (item.type) {
                    Report.Type.TODAY -> activity.getString(R.string.today)
                    Report.Type.THIS_WEEK -> activity.getString(R.string.this_week)
                    Report.Type.THIS_MONTH -> activity.getString(R.string.this_month)
                }

                if (item.orders.isEmpty()) {
                    // Disable clickability when there is no orders
                    this.root.isClickable = false

                    // Hide chart when there is no orders
                    this.charts.visibility = View.GONE
                } else {
                    setupBarChart(this.barChart, item)
                    setBarChartData(this.barChart, item)
                }
            }
        }

        private fun setupBarChart(barChart: BarChart, report: Report) {
            val textPrimaryColor = activity.getThemeColor(android.R.attr.textColorPrimary)
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
            val dataSet = BarDataSet(getBarDataValues(report), "").apply {
                setDrawIcons(false)
                this.valueFormatter = ValueOverBarFormatter()
                this.color = ContextCompat.getColor(activity, R.color.colorPrimary)
            }
            barChart.data = BarData(dataSet)
        }

        private fun initZerosArray(report: Report): IntArray =
            when (report.type) {
                Report.Type.TODAY -> {
                    IntArray(24) { 0 }
                }
                Report.Type.THIS_WEEK -> {
                    IntArray(7) { 0 }
                }
                Report.Type.THIS_MONTH -> {
                    when (report.orders[0].timeCreated.get(Calendar.MONTH)) {
                        Calendar.JANUARY, Calendar.MARCH, Calendar.MAY, Calendar.JULY, Calendar.AUGUST, Calendar.OCTOBER, Calendar.DECEMBER -> {
                            IntArray(31) { 0 }
                        }
                        Calendar.APRIL, Calendar.JUNE, Calendar.SEPTEMBER, Calendar.NOVEMBER -> {
                            IntArray(30) { 0 }
                        }
                        else -> {
                            val cal = Calendar.getInstance().apply {
                                set(Calendar.YEAR, report.orders[0].timeCreated.get(Calendar.YEAR))
                            }

                            if (cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365) {
                                IntArray(29) { 0 }
                            } else {
                                IntArray(28) { 0 }
                            }
                        }
                    }
                }
            }

        private fun initData(report: Report): IntArray {
            val emptyDataSet = initZerosArray(report)
            report.orders.forEach { order ->
                when (report.type) {
                    Report.Type.TODAY -> {
                        emptyDataSet[order.timeCreated.get(Calendar.HOUR_OF_DAY)]++
                    }
                    Report.Type.THIS_WEEK -> {
                        // Java Calendar DAY_OF_MONTH starts at 1, which is Sunday
                        emptyDataSet[order.timeCreated.get(Calendar.DAY_OF_WEEK) - 1]++
                    }
                    Report.Type.THIS_MONTH -> {
                        // Java Calendar DAY_OF_MONTH starts at 1
                        emptyDataSet[order.timeCreated.get(Calendar.DAY_OF_MONTH) - 1]++
                    }

                }

                if (order.isTakeout) {
                    takeOutCount++
                } else {
                    diningInCount++
                }
            }
            return emptyDataSet
        }

        private fun getBarDataValues(report: Report): ArrayList<BarEntry> {

            val values = ArrayList<BarEntry>()
            var index = 0f

            initData(report).forEach {
                values.add(BarEntry(index++, it.toFloat()))
            }

            return values
        }
    }

    private class ReportDiffCallback : DiffUtil.ItemCallback<Report>() {

        override fun areItemsTheSame(oldItem: Report, newItem: Report): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Report, newItem: Report): Boolean =
            oldItem == newItem
    }
}