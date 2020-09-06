package com.yue.ordernow.fragments


import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
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

    private lateinit var adapter: ReportAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val binding = FragmentDashboardBinding.inflate(inflater, container, false).apply {
            adapter = ReportAdapter(this@DashboardFragment)
            this.reports.adapter = adapter
            subscribeUi(adapter)
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_empty, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun subscribeUi(adapter: ReportAdapter) {
        viewModel.monthlyOrders.observe(viewLifecycleOwner) {
            val reportToday = Report(Report.Type.TODAY, 0, 0f)
            val reportWeek = Report(Report.Type.THIS_WEEK, 0, 0f)
            val reportMonth = Report(Report.Type.THIS_MONTH, 0, 0f)
            it.forEach { order ->
                if (order.timeCreated.get(Calendar.DAY_OF_YEAR) == viewModel.now.get(Calendar.DAY_OF_YEAR)) {
                    reportToday.quantity++
                    reportToday.amount += order.subtotalAmount
                    reportToday.orders.add(order)
                }

                if (order.timeCreated.get(Calendar.WEEK_OF_YEAR) == viewModel.now.get(Calendar.WEEK_OF_YEAR)) {
                    reportWeek.quantity++
                    reportWeek.amount += order.subtotalAmount
                    reportWeek.orders.add(order)
                }

                if (order.timeCreated.get(Calendar.MONTH) == viewModel.now.get(Calendar.MONTH)) {
                    reportMonth.quantity++
                    reportMonth.amount += order.subtotalAmount
                    reportMonth.orders.add(order)
                }
            }

            adapter.submitList(ArrayList<Report>().apply {
                this.add(reportToday)
                this.add(reportWeek)
                this.add(reportMonth)
            })
        }
    }

    override fun onClick(type: Report.Type, takeoutCount: Int, diningInCount: Int) {
        navigateToReport(type, takeoutCount, diningInCount)
    }

    override fun requestContext(): Context = requireContext()

    private fun navigateToReport(type: Report.Type, takeoutCount: Int, diningInCount: Int) {
        val direction = DashboardFragmentDirections.actionNavDashboardToReportDetailFragment(
            type.value,
            takeoutCount,
            diningInCount,
            viewModel.now.timeInMillis
        )
        view?.findNavController()?.navigate(direction)
    }
}
