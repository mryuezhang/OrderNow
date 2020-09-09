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

    interface OrderValidityChangeListener {
        fun onChange(order: Order)
    }

    protected abstract fun filterList()
    protected var orderValidityChangeListener: OrderValidityChangeListener? = null

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
        var choice = if (order.isPaid) {
            1
        } else {
            0
        }
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.title_more_actions_order))
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                when (choice) {
                    // Unpaid
                    0 -> {
                        if (order.isPaid) {
                            order.isPaid = false

                            // Update view
                            adapter.notifyItemChanged(position)

                            // Update database
                            activityViewModel.updateOrder(order)

                            Snackbar.make(
                                requireView(),
                                resources.getString(R.string.text_order_changed_to_unpaid),
                                Snackbar.LENGTH_LONG
                            ).setAction(resources.getString(R.string.undo)) {
                                order.isPaid = true

                                // Update view
                                adapter.notifyItemChanged(position)

                                // Update database
                                activityViewModel.updateOrder(order)
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
                            activityViewModel.updateOrder(order)

                            Snackbar.make(
                                requireView(),
                                resources.getString(R.string.text_order_changed_to_paid),
                                Snackbar.LENGTH_LONG
                            ).setAction(resources.getString(R.string.undo)) {
                                order.isPaid = false

                                // Update view
                                adapter.notifyItemChanged(position)

                                // Update database
                                activityViewModel.updateOrder(order)
                            }.show()
                        }
                    }
                    // Invalid order
                    2 -> {
                        order.isValid = false
                        order.isPaid = false // invalid orders should be marked as unpaid

                        // Update database
                        updateOrderAndSaleSummaries(order)

                        orderValidityChangeListener?.onChange(order)

                        // Update view
                        adapter.notifyItemChanged(position)

                        Snackbar.make(
                            requireView(),
                            resources.getString(R.string.text_order_change_to_invalid),
                            Snackbar.LENGTH_LONG
                        ).setAction(resources.getString(R.string.undo)) {
                            order.isValid = true

                            // Update database
                            updateOrderAndSaleSummaries(order)

                            // Update view
                            adapter.notifyItemChanged(position)
                        }.show()
                    }
                }
            }
            .setSingleChoiceItems(resources.getStringArray(R.array.order_statuses), choice) { _, which ->
                choice = which
            }
            .show()
    }
}