package com.yue.ordernow.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.yue.ordernow.R
import com.yue.ordernow.databinding.FragmentRecentOrdersBinding
import com.yue.ordernow.utilities.InjectorUtils
import com.yue.ordernow.viewModels.AbstractOrderListFragmentViewModel

class RecentOrdersFragment : AbstractOrderListFragment() {

    private lateinit var binding: FragmentRecentOrdersBinding
    override val viewModel: AbstractOrderListFragmentViewModel by viewModels {
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

    override fun filterList() {
        with(viewModel) {
            if (isFiltered()) {
                setQueryAllOrders()
                binding.noOrderHistoryText.text =
                    resources.getString(R.string.text_no_order_history)
                binding.noOrderHistoryText.textAlignment = TextView.TEXT_ALIGNMENT_TEXT_START
            } else {
                setQueryUnPaidOrders()
                binding.noOrderHistoryText.text =
                    resources.getString(R.string.text_no_unpaid_orders)
                binding.noOrderHistoryText.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            }
        }
    }

    override fun updateNoOrderHelpTextWhenSearching() {
        binding.noOrderHistoryText.text =
            resources.getString(R.string.text_no_orders)
        binding.noOrderHistoryText.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
    }
}