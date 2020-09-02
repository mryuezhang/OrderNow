package com.yue.ordernow.fragments

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.yue.ordernow.R
import com.yue.ordernow.adapters.OrderAdapter
import com.yue.ordernow.data.Order

abstract class AbstractFilterableOrderListFragment : AbstractOrderListFragment() {

    protected abstract fun filterList()

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_advance_order_list_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.action_filter_list -> {
                filterList()
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onLongClick(order: Order, adapter: OrderAdapter, position: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.title_more_actions_order))
            .setItems(resources.getStringArray(R.array.order_statuses)) { _, which ->
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
                    // Invalid order
                    2 -> {
                        order.isValid = false
                        order.isPaid = false // invalid orders should be marked as unpaid

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
            }
            .show()
    }
}