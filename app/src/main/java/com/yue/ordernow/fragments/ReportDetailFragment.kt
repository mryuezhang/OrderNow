package com.yue.ordernow.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.yue.ordernow.R
import com.yue.ordernow.activities.ReportDetailActivity
import com.yue.ordernow.adapters.OrderAdapter
import com.yue.ordernow.data.Report
import com.yue.ordernow.databinding.FragmentReportDetailBinding
import com.yue.ordernow.utilities.PercentFormatter

class ReportDetailFragment : Fragment() {

    private val hostActivity: ReportDetailActivity by lazy {
        activity as ReportDetailActivity
    }

    private lateinit var binding: FragmentReportDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportDetailBinding.inflate(inflater, container, false)
        context ?: return binding.root

        binding.reportSummary.layoutParams.height = resources.displayMetrics.widthPixels / 2
        setupOrderList(binding.orderList)
        setupToolBar(binding.toolbar)
        setupPieChart(binding.pieChart)
        addPieChartData(binding.pieChart)

        return binding.root
    }

    private fun setupToolBar(toolbar: Toolbar) {
        toolbar.title = when (hostActivity.viewModel.report.type) {
            Report.Type.TODAY -> resources.getString(R.string.today)
            Report.Type.THIS_WEEK -> resources.getString(R.string.this_week)
            Report.Type.THIS_MONTH -> resources.getString(R.string.this_month)
        }

        toolbar.setNavigationOnClickListener {
            activity?.finish()
            activity?.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    private fun setupOrderList(orderList: RecyclerView) {
        val adapter = OrderAdapter(hostActivity)
        orderList.adapter = adapter
        orderList.addItemDecoration(
            DividerItemDecoration(
                hostActivity,
                DividerItemDecoration.VERTICAL
            )
        )
        adapter.submitList(hostActivity.viewModel.report.orders)
    }

    private fun setupPieChart(pieChart: PieChart) {
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true
        pieChart.setEntryLabelTextSize(12f)
        pieChart.legend.isEnabled = false
        pieChart.holeRadius = 32f
        pieChart.transparentCircleRadius = 38f
        pieChart.animateY(1400, Easing.EaseInOutQuad)
    }

    private fun addPieChartData(pieChart: PieChart) {
        val dataEntries = ArrayList<PieEntry>(2).apply {
            if (hostActivity.viewModel.takeoutCount != 0) {
                add(
                    PieEntry(
                        hostActivity.viewModel.takeoutCount.toFloat(),
                        resources.getString(R.string.take_out)
                    )
                )
            }

            if (hostActivity.viewModel.diningInCount != 0) {
                add(
                    PieEntry(
                        hostActivity.viewModel.diningInCount.toFloat(),
                        resources.getString(R.string.dining_in)
                    )
                )
            }
        }

        val dataSet = PieDataSet(dataEntries, "").apply {
            setDrawIcons(false)
            color = ContextCompat.getColor(hostActivity, R.color.colorPrimary)
            valueTextColor = Color.WHITE
            valueTextSize = 12f
            sliceSpace = 2f
        }
        val data = PieData(dataSet).apply {
            setValueFormatter(PercentFormatter(pieChart))
        }
        pieChart.data = data
    }
}