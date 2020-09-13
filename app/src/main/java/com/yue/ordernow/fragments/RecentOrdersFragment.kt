package com.yue.ordernow.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.yue.ordernow.R
import com.yue.ordernow.activities.MainActivity
import com.yue.ordernow.databinding.FragmentRecentOrdersBinding
import com.yue.ordernow.utilities.InjectorUtils
import com.yue.ordernow.viewModels.AbstractOrderListFragmentViewModel
import com.yue.ordernow.viewModels.MainViewModel

class RecentOrdersFragment : AbstractFilterableOrderListFragment() {

    private lateinit var binding: FragmentRecentOrdersBinding
    override val viewModel: AbstractOrderListFragmentViewModel by viewModels {
        InjectorUtils.provideRecentOrderViewModelFactory(
            requireContext()
        )
    }

    override lateinit var activityViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentRecentOrdersBinding.inflate(inflater, container, false).run {
        if (activity is MainActivity) {
            activityViewModel = (activity as MainActivity).viewModel
        } else {
            throw IllegalAccessException("Illegal parent activity")
        }

        binding = this

        initOrderList(this.content.orderList, this.content.emptyListHelperText)
        setHasOptionsMenu(true)

        this.root
    }

    override fun filterList() {
        with(viewModel) {
            if (isFiltered()) {
                setQueryAllOrders()
                binding.content.emptyListHelperText.text =
                    resources.getString(R.string.text_no_order_history)
                binding.content.emptyListHelperText.textAlignment = TextView.TEXT_ALIGNMENT_TEXT_START
            } else {
                setQueryUnPaidOrders()
                binding.content.emptyListHelperText.text =
                    resources.getString(R.string.text_no_unpaid_orders)
                binding.content.emptyListHelperText.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            }
        }
    }

    override fun updateNoOrderHelpTextWhenSearching() {
        binding.content.emptyListHelperText.text =
            resources.getString(R.string.text_no_orders)
        binding.content.emptyListHelperText.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
    }
}