package com.yue.ordernow.fragments

import android.widget.TextView
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.yue.ordernow.R
import com.yue.ordernow.adapters.OrderAdapter
import com.yue.ordernow.data.Order
import com.yue.ordernow.viewModels.OrderListFragmentViewModel

abstract class OrderListFragment : Fragment(), OrderAdapter.ItemLongClickListener {

    abstract val viewModel: OrderListFragmentViewModel

    fun setupOrderList(orderList: RecyclerView, emptyTextView: TextView) {
        orderList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        val adapter = OrderAdapter(requireContext(), this)
        orderList.adapter = adapter

        viewModel.orders.observe(viewLifecycleOwner) { orders ->
            adapter.submitListWithHeaders(orders)
            emptyTextView.isGone = orders.isNotEmpty()
        }
    }

    override fun onLongClick(order: Order, adapter: OrderAdapter, position: Int) {
        MaterialAlertDialogBuilder(context)
            .setTitle(resources.getString(R.string.title_more_actions_order))
            .setItems(resources.getStringArray(R.array.order_statuses)) { dialog, which ->
                when (which) {
                    // Unpaid
                    0 -> {
                        if (order.isPaid) {
                            order.isPaid = false

                            // Update view
                            adapter.notifyItemChanged(position)

                            // Update database
                            viewModel.updateOrder(order)

                            Snackbar.make(
                                requireView(),
                                resources.getString(R.string.text_order_changed_to_unpaid),
                                Snackbar.LENGTH_LONG
                            ).setAction(resources.getString(R.string.undo)) {
                                order.isPaid = true

                                // Update view
                                adapter.notifyItemChanged(position)

                                // Update database
                                viewModel.updateOrder(order)
                            }.show()
                        }
                    }
                    // Paid
                    1 -> {
                        if (!order.isPaid) {
                            order.isPaid = true

                            // Update view
                            adapter.notifyItemChanged(position)

                            // Update database
                            viewModel.updateOrder(order)

                            Snackbar.make(
                                requireView(),
                                resources.getString(R.string.text_order_changed_to_paid),
                                Snackbar.LENGTH_LONG
                            ).setAction(resources.getString(R.string.undo)) {
                                order.isPaid = false

                                // Update view
                                adapter.notifyItemChanged(position)

                                // Update database
                                viewModel.updateOrder(order)
                            }.show()
                        }
                    }
                    2 -> {

                    }
                }
            }
            .show()
    }
}