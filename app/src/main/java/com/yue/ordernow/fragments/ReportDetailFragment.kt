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
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.yue.ordernow.R
import com.yue.ordernow.data.Order
import com.yue.ordernow.data.Report
import com.yue.ordernow.databinding.FragmentReportDetailBinding
import com.yue.ordernow.utilities.InjectorUtils
import com.yue.ordernow.utilities.PercentFormatter
import com.yue.ordernow.utilities.getThemeColor
import com.yue.ordernow.viewModels.ReportDetailViewModel

class ReportDetailFragment : AbstractFilterableOrderListFragment() {
    private val args: ReportDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentReportDetailBinding

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
    ): View? {
        binding = FragmentReportDetailBinding.inflate(inflater, container, false)

        setupOrderList(binding.orderList, binding.textNoUnpaidOrders)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.title =
            when (args.StringArgReportType) {
                Report.Type.TODAY.value -> resources.getString(R.string.today)
                Report.Type.THIS_WEEK.value -> resources.getString(R.string.this_week)
                Report.Type.THIS_MONTH.value -> resources.getString(R.string.this_month)
                else -> resources.getString(R.string.no)
            }
        initPieChart(binding.pieChart)
        setPieChartData(binding.pieChart)
        setupCustomLegend(
            binding.diningInQty,
            binding.takeoutQty,
            binding.totalQty
        )
        orderValidityChangeListener = object : OrderValidityChangeListener {
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
        return binding.root
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