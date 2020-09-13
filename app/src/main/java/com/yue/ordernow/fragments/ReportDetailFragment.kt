package com.yue.ordernow.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.yue.ordernow.R
import com.yue.ordernow.activities.MainActivity
import com.yue.ordernow.data.Order
import com.yue.ordernow.data.Report
import com.yue.ordernow.databinding.FragmentReportDetailBinding
import com.yue.ordernow.utilities.*
import com.yue.ordernow.viewModels.MainViewModel
import com.yue.ordernow.viewModels.ReportDetailViewModel

class ReportDetailFragment : AbstractFilterableOrderListFragment() {
    private val args: ReportDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentReportDetailBinding
    override lateinit var activityViewModel: MainViewModel

    override val viewModel: ReportDetailViewModel by viewModels {
        InjectorUtils.provideReportDetailFragmentViewModelFactory(
            requireContext(),
            args
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentReportDetailBinding.inflate(inflater, container, false).run {

        if (activity is MainActivity) {
            activityViewModel = (activity as MainActivity).viewModel
        } else {
            throw IllegalAccessException("Illegal parent activity")
        }

        binding = this

        initOrderList(binding.orderList, binding.textNoUnpaidOrders)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.title =
            when (args.StringArgReport.type) {
                Report.Type.TODAY -> resources.getString(R.string.today)
                Report.Type.THIS_WEEK -> resources.getString(R.string.this_week)
                Report.Type.THIS_MONTH -> resources.getString(R.string.this_month)
            }
        initPieChart(binding.pieChart)
        setPieChartData(binding.pieChart)
        setupCustomLegend(
            binding.diningInQty,
            binding.takeoutQty,
            binding.totalQty
        )
        initBarChart()
        setBarChartData()
        invalidateOrderListener = object : InvalidateOrderListener {
            override fun onChange(order: Order) {
                if (order.isValid) {
                    if (order.isTakeout) {
                        viewModel.takeOutCount++
                    } else {
                        viewModel.diningInCount++
                    }
                } else {
                    if (order.isTakeout) {
                        viewModel.takeOutCount--
                    } else {
                        viewModel.diningInCount--
                    }
                }

                setPieChartData(binding.pieChart)
                setupCustomLegend(
                    binding.diningInQty,
                    binding.takeoutQty,
                    binding.totalQty
                )
                binding.pieChart.invalidate()
            }
        }
        binding.root
    }

    /*
     * Private methods
     */

    private fun initPieChart(pieChart: PieChart) {
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true
        pieChart.setDrawEntryLabels(false)
        pieChart.setHoleColor(requireActivity().getThemeColor(android.R.attr.colorBackground))
        pieChart.animateY(1400, Easing.EaseInOutQuad)
        pieChart.legend.isEnabled = false
    }

    private fun setPieChartData(pieChart: PieChart) {
        val dataEntries = ArrayList<PieEntry>(2).apply {
            add(
                PieEntry(
                    viewModel.takeOutCount.toFloat(),
                    resources.getString(R.string.take_out)
                )
            )
            add(
                PieEntry(
                    viewModel.diningInCount.toFloat(),
                    resources.getString(R.string.dining_in)
                )
            )
        }

        val dataSet = PieDataSet(dataEntries, "").apply {
            setDrawIcons(false)
            colors = listOf(
                ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark),
                ContextCompat.getColor(requireContext(), R.color.colorPrimary)
            )
            valueTextColor = Color.WHITE
            valueTextSize = 12f
            sliceSpace = 3f
        }

        val data = PieData(dataSet).apply {
            setValueFormatter(PercentFormatter(pieChart))
        }

        pieChart.data = data
    }

    private fun initBarChart() {
        val textPrimaryColor = this.requireContext().getThemeColor(android.R.attr.textColorPrimary)
        binding.barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.barChart.xAxis.setDrawGridLines(false)
        binding.barChart.xAxis.granularity = 1f
        binding.barChart.xAxis.textColor = textPrimaryColor

        //binding.barChart.xAxis.setLabelCount(12, false)


        binding.barChart.axisLeft.setDrawGridLines(false)
//            barChart.axisRight.setDrawGridLines(false)
        binding.barChart.axisLeft.granularity = 1f
        binding.barChart.axisRight.granularity = 1f
        binding.barChart.axisLeft.axisMinimum = 0f
        binding.barChart.axisRight.axisMinimum = 0f
        binding.barChart.axisLeft.textColor = textPrimaryColor
        binding.barChart.axisRight.textColor = textPrimaryColor

        binding.barChart.setFitBars(true)
        binding.barChart.description.isEnabled = false
        binding.barChart.animateY(800)
        binding.barChart.legend.isEnabled = false
        binding.barChart.description.isEnabled = false
    }

    private fun setBarChartData() {
        val barChartLabels = ArrayList<String>()
        val dataEntries = ArrayList<BarEntry>()
        var count = 0f
        args.StringArgReport.saleData
            .toList()
            .sortedBy { it.second }
            .forEach {
                barChartLabels.add(it.first)
                dataEntries.add(BarEntry(count++, it.second.toFloat()))
            }
        binding.barChart.xAxis.valueFormatter = MenuItemNameFormatter(barChartLabels)
        binding.barChart.xAxis.setLabelCount(count.toInt(), false)
        val dataSet =  BarDataSet(dataEntries, "").apply {
            setDrawIcons(false)
            this.valueFormatter = ValueOverBarFormatter()
            this.color = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        }
        binding.barChart.data = BarData(dataSet)
    }

    private fun setupCustomLegend(
        diningInQty: TextView,
        takeoutQty: TextView,
        totalQty: TextView
    ) {
        diningInQty.text = viewModel.takeOutCount.toString()
        takeoutQty.text = viewModel.diningInCount.toString()
        totalQty.text =
            (viewModel.takeOutCount + viewModel.diningInCount).toString()
    }

    override fun filterList() {
        with(viewModel) {
            if (isFiltered()) {
                setQueryAllOrders()
            } else {
                setQueryUnPaidOrders()
            }
        }
    }

    override fun updateNoOrderHelpTextWhenSearching() {
        binding.textNoUnpaidOrders.text =
            resources.getString(R.string.text_no_orders)
    }
}