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
    ): View? = ContentOrderDetailBinding.inflate(inflater, container, false).run {
        this.order = args.order
        executePendingBindings()

        setupOrderItemList(this.orderItemList)

        this.orderType.text = if (args.order.isTakeout) {
            resources.getString(R.string.take_out)
        } else {
            resources.getString(R.string.dining_in)
        }

        this.orderNumber.text = formatOrderNumber(args.order.orderNumber)

        this.orderer.text = args.order.orderer

        this.taxRate.text = resources.getString(
            R.string.title_tax,
            (args.order.taxRate * 100).toInt().toString()
        )

        this.payStatus.text = if (args.order.isPaid) {
            resources.getString(R.string.paid)
        } else {
            resources.getString(R.string.unpaid)
        }

        this.root
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

    private fun formatOrderNumber(orderNumber: Int) = when {
        orderNumber in 0..9 -> "00$orderNumber"
        orderNumber in 10..99 -> "0$orderNumber"
        else -> orderNumber.toString()
    }
}