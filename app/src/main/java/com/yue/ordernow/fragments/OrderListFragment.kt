package com.yue.ordernow.fragments


import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.yue.ordernow.R
import com.yue.ordernow.list.OrderListAdapter
import com.yue.ordernow.models.OrderItem
import com.yue.ordernow.utils.currencyFormat
import kotlinx.android.synthetic.main.fragment_order_list.*

private const val ORDERS = "orderItems"
private const val TOTAL_AMOUNT = "total_amount"


class OrderListFragment : Fragment() {

    private var orderItems: ArrayList<OrderItem>? = null
    private var totalAmount: Float? = null
    private var listener: OnOrderListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderItems = it.getParcelableArrayList(ORDERS)
            totalAmount = it.getFloat(TOTAL_AMOUNT)
        }
        setHasOptionsMenu(true)
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
        order_list.adapter = OrderListAdapter(orderItems!!)
        total_amount.text = currencyFormat(totalAmount!!)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnOrderListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_order_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_clear -> {

            // update recycler view
            orderItems?.clear()
            order_list.adapter?.notifyDataSetChanged()

            // update text view
            totalAmount = 0F
            total_amount.text = currencyFormat(totalAmount!!)

            // replace fragment
            listener?.onOrderListFragmentInteraction()
            true
        }
        R.id.action_send -> {
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    interface OnOrderListFragmentInteractionListener {
        fun onOrderListFragmentInteraction()
    }


    companion object {

        @JvmStatic
        fun newInstance(orderItems: ArrayList<OrderItem>, totalAmount: Float) =
            OrderListFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ORDERS, orderItems)
                    putFloat(TOTAL_AMOUNT, totalAmount)
                }
            }
    }
}
