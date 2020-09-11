package com.yue.ordernow.fragments

import android.view.Menu
import android.view.MenuInflater
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.view.isGone
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.yue.ordernow.R
import com.yue.ordernow.adapters.OrderAdapter
import com.yue.ordernow.data.Order
import com.yue.ordernow.viewModels.AbstractOrderListFragmentViewModel
import com.yue.ordernow.viewModels.MainViewModel

abstract class AbstractOrderListFragment : AbstractSaleSummaryFragment(), OrderAdapter.ItemLongClickListener {

    protected abstract val viewModel: AbstractOrderListFragmentViewModel
    protected abstract var activityViewModel: MainViewModel
    protected abstract fun updateNoOrderHelpTextWhenSearching()

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_order_list_fragment, menu)
        val searchView = (menu.findItem(R.id.action_search).actionView as SearchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
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

    protected fun subscribeUi(orderList: RecyclerView, emptyTextView: TextView) {
        setupOrderList(orderList, emptyTextView)

        subscribeAndInit(activityViewModel)
    }

    protected fun updateOrderAndSaleSummaries(order: Order) {
        activityViewModel.updateOrder(order)
        if (order.isValid) {
            dailySaleSummary.addSaleData(order)
            weeklySaleSummary.addSaleData(order)
            monthlySaleSummary.addSaleData(order)
        } else {
            dailySaleSummary.removeSaleData(order)
            weeklySaleSummary.removeSaleData(order)
            monthlySaleSummary.removeSaleData(order)
        }
        activityViewModel.updateSaleSummary(dailySaleSummary)
        activityViewModel.updateSaleSummary(weeklySaleSummary)
        activityViewModel.updateSaleSummary(monthlySaleSummary)
    }

    private fun setupOrderList(orderList: RecyclerView, emptyTextView: TextView) {
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