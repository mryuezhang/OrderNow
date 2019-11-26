package com.yue.ordernow.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import com.yue.ordernow.R
import com.yue.ordernow.databinding.FragmentRecentOrdersBinding
import com.yue.ordernow.utilities.InjectorUtils
import com.yue.ordernow.viewModels.OrderListFragmentViewModel

class RecentOrdersFragment : OrderListFragment() {

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
        val binding = FragmentRecentOrdersBinding.inflate(inflater, container, false).apply {
            setupOrderList(this.historyList, this.noOrderHistoryText)
            setHasOptionsMenu(true)
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