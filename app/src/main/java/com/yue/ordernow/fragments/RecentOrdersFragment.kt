package com.yue.ordernow.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.yue.ordernow.R
import com.yue.ordernow.databinding.FragmentRecentOrdersBinding
import com.yue.ordernow.utilities.InjectorUtils
import com.yue.ordernow.viewModels.OrderListFragmentViewModel

class RecentOrdersFragment : OrderListFragment() {

    private lateinit var binding: FragmentRecentOrdersBinding
    override val viewModel: OrderListFragmentViewModel by viewModels {
        InjectorUtils.provideRecentOrderViewModelFactory(
            requireContext()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecentOrdersBinding.inflate(inflater, container, false)

        setupOrderList(binding.historyList, binding.noOrderHistoryText)
        setHasOptionsMenu(true)

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

    private fun filterList() {
        with(viewModel) {
            if (isFiltered()) {
                setQueryAllOrders()
                binding.noOrderHistoryText.text =
                    resources.getString(R.string.text_no_order_history)
            } else {
                setQueryUnPaidOrders()
                binding.noOrderHistoryText.text =
                    resources.getString(R.string.text_no_unpaid_orders)
            }
        }
    }
}