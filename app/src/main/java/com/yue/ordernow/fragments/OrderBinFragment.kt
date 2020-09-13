package com.yue.ordernow.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.yue.ordernow.R
import com.yue.ordernow.adapters.OrderAdapter
import com.yue.ordernow.data.Order
import com.yue.ordernow.databinding.FragmentOrderBinBinding
import com.yue.ordernow.utilities.InjectorUtils
import com.yue.ordernow.viewModels.AbstractOrderListFragmentViewModel
import com.yue.ordernow.viewModels.MainViewModel

class OrderBinFragment : AbstractOrderListFragment() {

    private lateinit var binding: FragmentOrderBinBinding
    override lateinit var activityViewModel: MainViewModel
    override val viewModel: AbstractOrderListFragmentViewModel by viewModels {
        InjectorUtils.provideOrderBinViewModelFactory(
            requireContext()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentOrderBinBinding.inflate(inflater, container, false).run {
        binding = this
        initOrderList(this.content.orderList, this.content.emptyListHelperText)
        setHasOptionsMenu(true)

        this.root
    }

    override fun updateNoOrderHelpTextWhenSearching() {
        binding.content.emptyListHelperText.text =
            resources.getString(R.string.text_no_orders)
        binding.content.emptyListHelperText.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
    }

    override fun onLongClick(order: Order, adapter: OrderAdapter, position: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.title_validate_order))
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(resources.getString(R.string.validate)) { _, _ ->
                order.isValid = true

                // Update database
                activityViewModel.updateOrder(order)

                // Update view
                adapter.notifyItemChanged(position)

                Snackbar.make(
                    requireView(),
                    resources.getString(R.string.text_order_validated),
                    Snackbar.LENGTH_LONG
                ).setAction(resources.getString(R.string.undo)) {
                    order.isValid = false

                    // Update database
                    activityViewModel.updateOrder(order)

                    // Update view
                    adapter.notifyItemChanged(position)
                }.show()
            }
            .show()
    }
}