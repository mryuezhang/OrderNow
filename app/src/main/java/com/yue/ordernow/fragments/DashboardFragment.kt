package com.yue.ordernow.fragments


import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.yue.ordernow.R
import com.yue.ordernow.adapters.ReportAdapter
import com.yue.ordernow.data.Report
import com.yue.ordernow.databinding.FragmentDashboardBinding
import com.yue.ordernow.utilities.InjectorUtils
import com.yue.ordernow.viewModels.DashboardViewModel
import java.util.*
import kotlin.collections.ArrayList


class DashboardFragment : Fragment(), ReportAdapter.ReportClickListener {

    private val viewModel: DashboardViewModel by viewModels {
        InjectorUtils.provideDashboardViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentDashboardBinding.inflate(inflater, container, false).run {
        setHasOptionsMenu(true)
        val adapter = ReportAdapter(this@DashboardFragment)
        this.reports.adapter = adapter
        subscribeUi(adapter)

        this.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_empty, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun subscribeUi(adapter: ReportAdapter) {
        val reportToday = Report(Report.Type.TODAY, 0, 0f)
        val reportWeek = Report(Report.Type.THIS_WEEK, 0, 0f)
        val reportMonth = Report(Report.Type.THIS_MONTH, 0, 0f)
        val reports = ArrayList<Report>().apply {
            addAll(listOf(reportToday, reportWeek, reportMonth))
        }

        // Since ReportDetailFragment can modify our order list(toggle orders' validity),
        // the observer cannot be view life cycle aware. This is because if order list is
        // modified within ReportDetailFragment, observer won't observe the change since
        // DashboardFragment's view is not visible at the moment
        viewModel.monthlyOrders.observeForever { list ->
            list.forEach {
                if (viewModel.now.get(Calendar.DAY_OF_YEAR) == it.timeCreated.get(Calendar.DAY_OF_YEAR)) {
                    reportToday.associate(it)
                }
                if (viewModel.now.get(Calendar.WEEK_OF_YEAR) == it.timeCreated.get(Calendar.WEEK_OF_YEAR)) {
                    reportWeek.associate(it)
                }
                reportMonth.associate(it)
            }
            adapter.submitList(reports)
        }
    }

    override fun onClick(report: Report) {
        navigateToReport(report)
    }

    override fun requestContext(): Context = requireContext()

    private fun navigateToReport(report: Report) {
        val direction = DashboardFragmentDirections.actionNavDashboardToReportDetailFragment(report)
        view?.findNavController()?.navigate(direction)
    }
}
