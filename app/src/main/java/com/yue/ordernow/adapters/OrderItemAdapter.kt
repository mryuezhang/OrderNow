package com.yue.ordernow.adapters

import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yue.ordernow.R
import com.yue.ordernow.data.OrderItem
import com.yue.ordernow.databinding.ListItemOrderItemBinding
import com.yue.ordernow.fragments.RestaurantMenuFragment


class OrderItemAdapter(private val listener: OrderItemOnClickListener?) :
    ListAdapter<OrderItem, RecyclerView.ViewHolder>(OrderItemDiffCallback()) {

    interface OrderItemOnClickListener {
        fun onClick(orderItem: OrderItem, position: Int)
    }

    companion object {
        const val TYPE_DEFAULT = 0
        const val TYPE_WITH_NOTE = 1
        const val TYPE_WITH_EXTRA_COST = 2
        const val TYPE_WITH_NOTE_AND_EXTRA_COST = 3
    }

    override fun getItemViewType(position: Int): Int {
        val orderItem = getItem(position)

        if (orderItem.extraCost != 0f && orderItem.note == "") {
            return TYPE_WITH_EXTRA_COST
        }

        if (orderItem.extraCost == 0f && orderItem.note != "") {
            return TYPE_WITH_NOTE
        }

        if (orderItem.extraCost != 0f && orderItem.note != "") {
            return TYPE_WITH_NOTE_AND_EXTRA_COST
        }

        return TYPE_DEFAULT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ListItemOrderItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        // Hide certain UI components based on view types
        when (viewType) {
            TYPE_WITH_NOTE -> {
                binding.extraCostTitle.height = 0
                binding.extraCost.height = 0
            }
            TYPE_WITH_EXTRA_COST -> {
                binding.note.height = 0
            }
            TYPE_DEFAULT -> {
                binding.extraCostTitle.height = 0
                binding.extraCost.height = 0
                binding.note.height = 0
            }
        }

        return OrderItemViewHolder(binding)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val menuItem = getItem(position)
        (holder as OrderItemViewHolder).bind(menuItem, position)
    }

    inner class OrderItemViewHolder(val binding: ListItemOrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderItem, position: Int) {
            binding.apply {
                orderItem = item
                if (listener != null) {
                    onClickListener = View.OnClickListener {
                        listener.onClick(item, position)
                    }
                } else {
                    // Disable clickability when there is no listener
                    this.foreground.foreground = null
                }
                executePendingBindings()
            }
        }
    }

    class OrderItemSwipeHelper(private val listener: OrderItemSwipeListener) :
        ItemTouchHelper.Callback() {

        interface OrderItemSwipeListener {
            fun onSwipe(itemPosition: Int)
        }

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int = makeMovementFlags(0, LEFT or RIGHT)

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = false

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            listener.onSwipe(viewHolder.adapterPosition)
        }

        override fun onChildDrawOver(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder?,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            // Only redraw foreground
            getDefaultUIUtil().onDrawOver(
                c,
                recyclerView,
                (viewHolder as OrderItemViewHolder).binding.foreground,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {

            // Get the current constraints
            val constraintSet = ConstraintSet()
            constraintSet.clone((viewHolder as OrderItemViewHolder).binding.background)

            // Get predefined margin in pixel
            val margin =
                (listener as RestaurantMenuFragment).resources.getDimension(R.dimen.order_item_background_icon_margin)

            // Change the constraints based on swipe left or right
            if (dX > 0) {
                // Clear the current end constraint
                constraintSet.clear(
                    R.id.imageView,
                    ConstraintSet.END
                )

                // Make new constraint
                constraintSet.connect(
                    R.id.imageView,
                    ConstraintSet.START,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.START,
                    margin.toInt()
                )
            } else {
                // Clear the current end constraint
                constraintSet.clear(
                    R.id.imageView,
                    ConstraintSet.START
                )
                // Make new constraint
                constraintSet.connect(
                    R.id.imageView,
                    ConstraintSet.END,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.END,
                    margin.toInt()
                )
            }

            // Apply the new constraints
            constraintSet.applyTo(viewHolder.binding.background)

            // Only redraw foreground
            getDefaultUIUtil().onDraw(
                c,
                recyclerView,
                viewHolder.binding.foreground,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            // Only clear foreground
            getDefaultUIUtil().clearView((viewHolder as OrderItemViewHolder).binding.foreground)
        }
    }

    private class OrderItemDiffCallback : DiffUtil.ItemCallback<OrderItem>() {

        override fun areItemsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean =
            oldItem == newItem
    }

}