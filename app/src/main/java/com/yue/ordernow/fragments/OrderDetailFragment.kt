package com.yue.ordernow.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.yue.ordernow.R
import com.yue.ordernow.adapters.OrderItemAdapter
import com.yue.ordernow.databinding.FragmentOrderDetailBinding

class OrderDetailFragment : Fragment() {

    private val args: OrderDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentOrderDetailBinding.inflate(inflater, container, false).apply {
            this.order = args.order
            executePendingBindings()

            setupToolbar(this.toolbar)
            setupOrderItemList(this.orderItemList)

            this.textTax.text =
                resources.getString(
                    R.string.title_tax,
                    (args.order.taxRate * 100).toInt().toString()
                )

            this.payStatus.text = if (args.order.isPaid) {
                resources.getString(R.string.paid)
            } else {
                resources.getString(R.string.unpaid)
            }
        }

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            android.R.id.home -> {
                view!!.findNavController().navigateUp()
                super.onOptionsItemSelected(item)
            }
            else -> false
        }

    /*
     * Private methods
     */

    private fun setupOrderItemList(list: RecyclerView) {
        val adapter = OrderItemAdapter(null)
        list.adapter = adapter

        list.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        adapter.submitList(args.order.orderItems)
    }

    private fun setupToolbar(toolbar: Toolbar) {
        toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }
        toolbar.title = resources.getString(R.string.title_order_detail)
    }
}