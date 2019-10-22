package com.yue.ordernow.fragments


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import com.yue.ordernow.R
import com.yue.ordernow.adapters.OrderAdapter
import com.yue.ordernow.databinding.FragmentOrderHistoryBinding
import com.yue.ordernow.utils.InjectorUtils
import com.yue.ordernow.viewModels.OrderHistoryViewModel

/**
 * A simple [Fragment] subclass.
 */
class OrderHistoryFragment : Fragment() {
    private val viewModel: OrderHistoryViewModel by viewModels {
        InjectorUtils.provideOrderHistoryViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        val binding = FragmentOrderHistoryBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val adapter = OrderAdapter(context!!)
        binding.orderHistory.adapter = adapter
        binding.orderHistory.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.materialButton.setOnClickListener {
            viewModel.deleteAllOrders()
        }
        subscribeUi(adapter)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_empty, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun subscribeUi(adapter: OrderAdapter) {
        viewModel.orders.observe(viewLifecycleOwner) { orders ->
            adapter.submitList(orders)
        }
    }

    private fun getDailyReport() {
        viewModel.dailyOrders.observe(viewLifecycleOwner) {
            Log.i("Daily", it.toString())
        }
    }

    private fun getWeeklyReport() {
        viewModel.weeklyOrders.observe(viewLifecycleOwner) {
            Log.i("Weekly", it.toString())
        }
    }

    private fun getMonthlyReport() {
        viewModel.monthlyOrders.observe(viewLifecycleOwner) {
            Log.i("Monthly", it.toString())
        }
    }

    private fun getYearlyReport() {
        viewModel.yearlyOrders.observe(viewLifecycleOwner) {
            Log.i("Yearly", it.toString())
        }
    }
}
