package com.yue.ordernow.fragments

import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
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
            val items = ArrayList<OrderAdapter.ListItem>()

            if (orders.isNotEmpty()) {
                items.add(OrderAdapter.Header(orders.first().getCreatedDate()))
                if (orders.first().getCreatedDate() == orders.last().getCreatedDate()) {
                    items.addAll(orders)
                } else {
                    orders.forEachIndexed { index, order ->
                        items.add(order)
                        if (index + 1 < orders.size &&
                            orders[index + 1].getCreatedDate() != order.getCreatedDate()
                        ) {
                            items.add(OrderAdapter.Header(orders[index + 1].getCreatedDate()))
                        }
                    }
                }
            }

            adapter.submitList(items)
            emptyTextView.isGone = orders.isNotEmpty()
        }
    }

    override fun onLongClick(order: Order, adapter: OrderAdapter, position: Int) {
        activity?.let {
            AlertDialog.Builder(it).apply {
                setTitle(resources.getString(R.string.title_mark_order_paid))
                setPositiveButton(R.string.yes) { _, _ ->
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
                setNegativeButton(R.string.no) { _, _ ->
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
            }.create().show()
        }
    }
}