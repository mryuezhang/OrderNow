package com.yue.ordernow.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.yue.ordernow.R
import com.yue.ordernow.adapters.OrderItemAdapter
import com.yue.ordernow.databinding.ContentOrderDetailBinding

class OrderDetailFragment : Fragment() {

    private val args: OrderDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ContentOrderDetailBinding.inflate(inflater, container, false).apply {
            this.order = args.order
            executePendingBindings()

            setupOrderItemList(this.orderItemList)

            this.textTax.text
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
}