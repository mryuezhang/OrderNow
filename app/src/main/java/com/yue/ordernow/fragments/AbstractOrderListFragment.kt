package com.yue.ordernow.fragments

import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.SearchView
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
import com.yue.ordernow.viewModels.AbstractOrderListFragmentViewModel

abstract class AbstractOrderListFragment : Fragment(), OrderAdapter.ItemLongClickListener {

    protected abstract val viewModel: AbstractOrderListFragmentViewModel
    protected abstract fun filterList()
    protected abstract fun updateNoOrderHelpTextWhenSearching()

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_report_detail_fragment, menu)
        val searchView = (menu.findItem(R.id.action_search).actionView as SearchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.i("QUERY", query)
                query?.let {
                    viewModel.setSearchText(it)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.setSearchText(it)
                }
                return true
            }
        })
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                updateNoOrderHelpTextWhenSearching()
            }
        }

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
                    2 -> {

                    }
                }
            }
            .show()
    }

    protected fun setupOrderList(orderList: RecyclerView, emptyTextView: TextView) {
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
}