package com.yue.ordernow.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.yue.ordernow.R
import com.yue.ordernow.list.OrderListAdapter
import com.yue.ordernow.models.MenuItem
import com.yue.ordernow.utils.currencyFormat
import kotlinx.android.synthetic.main.fragment_order_list.*

private const val ORDERS = "orders"
private const val TOTAL_AMOUNT = "total_amount"


class OrderListFragment : Fragment() {

    private var orders: ArrayList<MenuItem>? = null
    private var totalAmount: Float? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orders = it.getParcelableArrayList(ORDERS)
            totalAmount = it.getFloat(TOTAL_AMOUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_order_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        order_list.layoutManager = LinearLayoutManager(activity)
        order_list.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
        order_list.adapter = OrderListAdapter(orders!!)
        total_amount.text = currencyFormat(totalAmount!!)
    }


    companion object {

        @JvmStatic
        fun newInstance(orders: ArrayList<MenuItem>, totalAmount: Float) =
            OrderListFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ORDERS, orders)
                    putFloat(TOTAL_AMOUNT, totalAmount)
                }
            }
    }
}
