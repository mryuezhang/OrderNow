package com.yue.ordernow.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.yue.ordernow.R
import com.yue.ordernow.activities.ReportDetailActivity
import com.yue.ordernow.adapters.OrderAdapter
import com.yue.ordernow.data.Report
import com.yue.ordernow.databinding.FragmentReportDetailBinding

class ReportDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentReportDetailBinding.inflate(inflater, container, false)
        context ?: return binding.root

        activity?.let {
            val adapter = OrderAdapter(it)
            binding.orderList.adapter = adapter
            binding.orderList.addItemDecoration(
                DividerItemDecoration(
                    it,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter.submitList((activity as ReportDetailActivity).viewModel.report.orders)
        }

        binding.toolbar.title = when ((activity as ReportDetailActivity).viewModel.report.type) {
            Report.Type.TODAY -> resources.getString(R.string.today)
            Report.Type.THIS_WEEK -> resources.getString(R.string.this_week)
            Report.Type.THIS_MONTH -> resources.getString(R.string.this_month)
        }

        binding.toolbar.setNavigationOnClickListener {
            activity?.finish()
            activity?.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        return binding.root
    }
}