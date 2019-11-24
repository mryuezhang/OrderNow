package com.yue.ordernow.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.yue.ordernow.databinding.FragmentRecentOrdersBinding
import com.yue.ordernow.utilities.InjectorUtils
import com.yue.ordernow.viewModels.OrderListFragmentViewModel

class RecentOrdersFragment : OrderListFragment() {

    override val viewModel: OrderListFragmentViewModel by viewModels {
        InjectorUtils.provideOrderHistoryViewModelFactory(
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
}