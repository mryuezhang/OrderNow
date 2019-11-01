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
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.yue.ordernow.R
import com.yue.ordernow.activities.ReportDetailActivity
import com.yue.ordernow.data.Report
import com.yue.ordernow.databinding.ListItemReportBinding
import com.yue.ordernow.utilities.DayOfMonthFormatter
import com.yue.ordernow.utilities.DayOfWeekFormatter
import com.yue.ordernow.utilities.TimeFormatter
import com.yue.ordernow.utilities.ValueOverBarFormatter
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

                // Disable clickability when there is no orders
                if (item.orders.isEmpty()) {
                    this.root.isClickable = false
                    this.charts.visibility = View.GONE
                } else {
                    this.barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
                    this.barChart.xAxis.setDrawGridLines(false)
                    this.barChart.xAxis.granularity = 1f
                    this.barChart.xAxis.valueFormatter = when {
                        item.type == Report.Type.THIS_WEEK -> DayOfWeekFormatter()
                        item.type == Report.Type.TODAY -> TimeFormatter()
                        else -> DayOfMonthFormatter()
                    }

                    this.barChart.axisLeft.setDrawGridLines(false)
                    this.barChart.axisRight.setDrawGridLines(false)
                    this.barChart.axisLeft.granularity = 1f
                    this.barChart.axisRight.granularity = 1f

                    val dataSet = BarDataSet(getBarDataValues(item), "").apply {
                        setDrawIcons(false)
                        this.valueFormatter = ValueOverBarFormatter()
                        this.color = ContextCompat.getColor(activity, R.color.colorPrimary)
                    }
                    this.barChart.setFitBars(true)
                    this.barChart.description.isEnabled = false
                    this.barChart.animateY(800)
                    this.barChart.legend.isEnabled = false
                    this.barChart.data = BarData(dataSet)
                    this.barChart.description.isEnabled = false
                }

            }
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
                        emptyDataSet[order.timeCreated.get(Calendar.DAY_OF_WEEK)]++
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

        private fun setupBarChart() {

        }
    }

    private class ReportDiffCallback : DiffUtil.ItemCallback<Report>() {

        override fun areItemsTheSame(oldItem: Report, newItem: Report): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Report, newItem: Report): Boolean =
            oldItem == newItem
    }
}