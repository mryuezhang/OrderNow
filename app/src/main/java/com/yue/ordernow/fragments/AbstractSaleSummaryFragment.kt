package com.yue.ordernow.fragments

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.yue.ordernow.data.Report
import com.yue.ordernow.data.SaleSummary
import com.yue.ordernow.viewModels.MainViewModel
import java.util.*

abstract class AbstractSaleSummaryFragment : Fragment() {
    protected lateinit var dailySaleSummary: SaleSummary
    protected lateinit var weeklySaleSummary: SaleSummary
    protected lateinit var monthlySaleSummary: SaleSummary

    protected fun subscribeAndInit(mainViewModel: MainViewModel) {
        mainViewModel.saleSummaries.observe(viewLifecycleOwner) { list ->
            val tokens = mutableListOf(false, false, false)
            list.forEach { saleSummary ->
                when (saleSummary.type) {
                    Report.Type.TODAY -> {
                        Log.i("TODAY", saleSummary.subTotal.toString() + ", " + saleSummary.orderCount)
                        dailySaleSummary = saleSummary
                        tokens[0] = true
                    }
                    Report.Type.THIS_WEEK -> {
                        Log.i("WEEK", saleSummary.subTotal.toString() + ", " + saleSummary.orderCount)
                        weeklySaleSummary = saleSummary
                        tokens[1] = true
                    }
                    Report.Type.THIS_MONTH -> {
                        Log.i("MONTH", saleSummary.subTotal.toString() + ", " + saleSummary.orderCount)
                        monthlySaleSummary = saleSummary
                        tokens[2] = true
                    }
                }
            }
            for ((index, value) in tokens.withIndex()) {
                if (!value) {
                    when (index) {
                        0 -> {
                            dailySaleSummary = SaleSummary.newInstance(Report.Type.TODAY, Calendar.getInstance())
                            mainViewModel.saveToDatabase(dailySaleSummary)
                        }

                        1 -> {
                            weeklySaleSummary = SaleSummary.newInstance(Report.Type.THIS_WEEK, Calendar.getInstance())
                            mainViewModel.saveToDatabase(weeklySaleSummary)
                        }
                        2 -> {
                            monthlySaleSummary = SaleSummary.newInstance(Report.Type.THIS_MONTH, Calendar.getInstance())
                            mainViewModel.saveToDatabase(monthlySaleSummary)
                        }
                    }
                }
            }
        }
    }
}