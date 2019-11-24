package com.yue.ordernow.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.*
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
import com.yue.ordernow.data.Report
import com.yue.ordernow.databinding.FragmentReportDetailBinding
import com.yue.ordernow.utilities.InjectorUtils
import com.yue.ordernow.utilities.PercentFormatter
import com.yue.ordernow.utilities.getThemeColor
import com.yue.ordernow.viewModels.ReportDetailViewModel
import java.util.*
import kotlin.collections.ArrayList

class ReportDetailFragment : OrderListFragment() {

    private val args: ReportDetailFragmentArgs by navArgs()

    override val viewModel: ReportDetailViewModel by viewModels {
        InjectorUtils.provideReportDetailFragmentViewModelFactory(
            requireContext(),
            Report.Type.fromInt(args.StringArgReportType),
            Calendar.getInstance().apply {
                timeInMillis = args.StringArgTimeStamp
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentReportDetailBinding.inflate(inflater, container, false).apply {
            this.reportSummary.layoutParams.height = resources.displayMetrics.widthPixels / 2
            setupOrderList(this.orderList, this.textNoUnpaidOrders)
            setHasOptionsMenu(true)
            (activity as AppCompatActivity).supportActionBar?.title =
                when (args.StringArgReportType) {
                    Report.Type.TODAY.value -> resources.getString(R.string.today)
                    Report.Type.THIS_WEEK.value -> resources.getString(R.string.this_week)
                    Report.Type.THIS_MONTH.value -> resources.getString(R.string.this_month)
                    else -> resources.getString(R.string.no)
                }
            setupPieChart(this.pieChart)
            addPieChartData(this.pieChart)
            setupCustomLegend(
                this.diningInQty,
                this.takeoutQty,
                this.totalQty
            )
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_report_detail_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.action_filter_list -> {
                filterList()
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }

    /*
     * Private methods
     */

    private fun setupPieChart(pieChart: PieChart) {
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true
        pieChart.setDrawEntryLabels(false)
        pieChart.setHoleColor(requireActivity().getThemeColor(android.R.attr.colorBackground))
        pieChart.animateY(1400, Easing.EaseInOutQuad)
        pieChart.legend.isEnabled = false
    }

    private fun addPieChartData(pieChart: PieChart) {
        val dataEntries = ArrayList<PieEntry>(2).apply {
            add(
                PieEntry(
                    args.StringArgTakeoutCount.toFloat(),
                    resources.getString(R.string.take_out)
                )
            )
            add(
                PieEntry(
                    args.StringArgDiningInCount.toFloat(),
                    resources.getString(R.string.dining_in)
                )
            )
        }

        val dataSet = PieDataSet(dataEntries, "").apply {
            setDrawIcons(false)
            colors = listOf(
                ContextCompat.getColor(requireContext(), R.color.color_takeout),
                ContextCompat.getColor(requireContext(), R.color.color_dining_in)
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
        diningInQty.text = args.StringArgDiningInCount.toString()
        takeoutQty.text = args.StringArgTakeoutCount.toString()
        totalQty.text =
            (args.StringArgDiningInCount + args.StringArgTakeoutCount).toString()
    }

    private fun filterList() {
        with(viewModel) {
            if (isFiltered()) {
                setQueryAllOrders()
            } else {
                setQueryUnPaidOrders()
            }
        }
    }
}