package com.yue.ordernow.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yue.ordernow.R
import com.yue.ordernow.activities.REPORT
import com.yue.ordernow.activities.ReportDetailActivity
import com.yue.ordernow.data.Report
import com.yue.ordernow.databinding.ListItemReportBinding


class ReportAdapter(private val activity: Activity) :
    ListAdapter<Report, RecyclerView.ViewHolder>(ReportDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ReportViewHolder(
            ListItemReportBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val report = getItem(position)
        (holder as ReportViewHolder).bind(report)
    }

    private inner class ReportViewHolder(private val binding: ListItemReportBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setOnClickListener {
                binding.report?.let { report ->
                    val intent = Intent(activity, ReportDetailActivity::class.java)
                    intent.putExtra(REPORT, report)
                    activity.startActivity(intent)
                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }
        }

        fun bind(item: Report) {
            binding.apply {
                report = item
                executePendingBindings()

                // Set text using string resource here instead of using data binding for translatability
                title.text = when (item.type) {
                    Report.Type.TODAY -> activity.getString(R.string.today)
                    Report.Type.THIS_WEEK -> activity.getString(R.string.this_week)
                    Report.Type.THIS_MONTH -> activity.getString(R.string.this_month)
                }
            }
        }
    }

    private class ReportDiffCallback : DiffUtil.ItemCallback<Report>() {

        override fun areItemsTheSame(oldItem: Report, newItem: Report): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Report, newItem: Report): Boolean =
            oldItem == newItem
    }
}