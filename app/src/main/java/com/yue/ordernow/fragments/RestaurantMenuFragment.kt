package com.yue.ordernow.fragments


import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.yue.ordernow.R
import com.yue.ordernow.activities.MainActivity
import com.yue.ordernow.adapters.*
import com.yue.ordernow.data.MenuItem
import com.yue.ordernow.data.Order
import com.yue.ordernow.data.OrderItem
import com.yue.ordernow.databinding.FragmentRestaurantMenuBinding
import com.yue.ordernow.utilities.ZoomOutPageTransformer
import com.yue.ordernow.utilities.currencyFormat
import com.yue.ordernow.viewModels.MainViewModel
import java.util.ArrayList
import kotlin.Comparator

private const val IS_BOTTOM_SHEET_EXPAND = "ibse"

class RestaurantMenuFragment : Fragment(),
    CustomOrderDialogFragment.CustomOrderDialogListener,
    ModifyOrderDialogFragment.ModifyOrderDialogListener,
    MenuItemAdapter.MenuItemListener,
    OrderItemAdapter.OrderItemOnClickListener,
    OrderItemAdapter.OrderItemSwipeHelper.OrderItemSwipeListener {

    private val adapter = OrderItemAdapter(this)
    private lateinit var binding: FragmentRestaurantMenuBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var activityViewModel: MainViewModel
    private var isOptionMenuViable = false
    private val slideDownAnimation: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.slide_down
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        if (activity is MainActivity) {
            activityViewModel = (activity as MainActivity).viewModel
        } else {
            throw IllegalAccessException("Illegal parent activity")
        }

        binding = FragmentRestaurantMenuBinding.inflate(inflater, container, false)

        // Set adapter for menu page
        binding.menuPage.adapter = MenuPageViewAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.menuPage) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()
        binding.menuPage.setPageTransformer(ZoomOutPageTransformer())

        // Add divider between each list item
        binding.bottomSheet.orderList.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )

        // Set adapter
        binding.bottomSheet.orderList.adapter = adapter
        adapter.submitList(activityViewModel.orderItems)
        ItemTouchHelper(OrderItemAdapter.OrderItemSwipeHelper(this)).attachToRecyclerView(binding.bottomSheet.orderList)

        // Setup bottom sheet behavior
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.root)
        updateBottomSheetBehavior()
        setBottomSheetText()
        binding.bottomSheet.onHeaderClickListener = OnBottomSheetHeaderClickListener()
        bottomSheetBehavior.addBottomSheetCallback(BottomSheetCallback())
        binding.bottomSheet.orderType.setOnCheckedChangeListener { _, i ->
            activityViewModel.isTakeout = i == R.id.take_out
        }
        savedInstanceState?.getBoolean(IS_BOTTOM_SHEET_EXPAND)?.let {
            if (it) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                showExpandedBottomSheetShape()
                showOrderDetailOptionsMenu()
            }
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (::bottomSheetBehavior.isInitialized &&
            bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED
        ) {
            outState.putBoolean(IS_BOTTOM_SHEET_EXPAND, true)
        }

        super.onSaveInstanceState(outState)
    }


    /*
     * Options menu mthods
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.action_confirm).isVisible = isOptionMenuViable
        menu.findItem(R.id.action_clear).isVisible = isOptionMenuViable
    }

    @Suppress("UNCHECKED_CAST")
    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean = when (item.itemId) {
        R.id.action_confirm -> {
            // Sort all order items
            activityViewModel.orderItems.sortWith(Comparator { t, t2 ->
                t.item.name.compareTo(t2.item.name)
            })

            // Create order object and save it to data base
            // Note: clone the array list of order items here is a must
            // since inserting into data base is a asyc call, and we are clearing
            // out all the data right after with removeCurrentOrder(). Otherwise
            // empty array lists will be inserted into database
            activityViewModel.saveToDatabase(
                Order.newInstance(
                    activityViewModel.orderItems.clone() as ArrayList<OrderItem>,
                    activityViewModel.subtotal,
                    activityViewModel.totalQuantity,
                    activityViewModel.isTakeout,
                    activityViewModel.isPaid
                )
            )

            // Clear local copy of data and refresh view
            removeCurrentOrder()
            true
        }
        R.id.action_clear -> {
            activity?.let {
                AlertDialog.Builder(it).apply {
                    setMessage(resources.getString(R.string.title_confirm_discard_order))
                    setPositiveButton(R.string.discard) { _, _ ->
                        removeCurrentOrder()
                    }
                    setNegativeButton(R.string.cancel) { dialog, _ ->
                        dialog.cancel()
                    }
                }.create().show()
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    /*
     * CustomOrderDialogFragment.CustomOrderDialogListener method
     */

    override fun onDialogPositiveClick(dialog: DialogFragment, orderItem: OrderItem) {
        addOrder(null, orderItem)
    }

    /*
     * MenuItemAdapter.MenuItemListener methods
     */

    override fun onOrderButtonClick(menuItem: MenuItem?) {
        menuItem?.let {
            addOrder(null, OrderItem(it, 1, ""))
        }
    }

    override fun onCustomizeButtonClick(menuItem: MenuItem?) {
        menuItem?.let {
            CustomOrderDialogFragment(this, it.copy()) // MUST pass a copy here
                .show(childFragmentManager, CustomOrderDialogFragment.TAG)
        }
    }

    /*
     * OrderItemOnClickListener method
     */
    override fun onClick(orderItem: OrderItem, position: Int) {
        ModifyOrderDialogFragment(this, orderItem, position).show(
            childFragmentManager,
            ModifyOrderDialogFragment.TAG
        )
    }

    /*
     * ModifyOrderDialogFragment.ModifyOrderDialogListener method
     */
    override fun onDialogPositiveClick(
        position: Int, quantityDiff: Int, extraCostDiff: Float
    ) {
        adapter.notifyItemChanged(position)

        if (quantityDiff == 0) {
            if (extraCostDiff != 0f) {
                activityViewModel.subtotal += extraCostDiff
            }
        } else {
            activityViewModel.totalQuantity += quantityDiff

            if (extraCostDiff == 0f) {
                activityViewModel.subtotal += quantityDiff * adapter.currentList[position].item.price
            } else {
                activityViewModel.subtotal += quantityDiff * adapter.currentList[position].item.price + extraCostDiff
            }
        }

        setBottomSheetText()
    }

    /*
     * OrderItemSwipeHelper.OrderItemSwipeListener method
     */
    override fun onSwipe(itemPosition: Int) {

        val orderItem = activityViewModel.orderItems[itemPosition]

        // Calculate the quantity and subtotal
        activityViewModel.totalQuantity -= orderItem.quantity
        activityViewModel.subtotal -= orderItem.getAmount()

        // Remove the item
        activityViewModel.orderItems.removeAt(itemPosition)
        binding.bottomSheet.orderList.adapter?.notifyItemRemoved(itemPosition)

        if (activityViewModel.orderItems.isEmpty()) {
            activity?.let {
                AlertDialog.Builder(it).apply {
                    setTitle(resources.getString(R.string.title_confirm_discard_order))
                    setMessage(resources.getString(R.string.message_discard_order))
                    setPositiveButton(R.string.discard) { _, _ ->
                        updateBottomSheetBehavior()
                    }
                    setNegativeButton(R.string.cancel) { dialog, _ ->
                        addOrder(itemPosition, orderItem)
                        dialog.cancel()
                    }
                }.create().show()
            }
        } else {
            Snackbar.make(
                binding.bottomSheet.body,
                resources.getString(R.string.text_order_item_removed),
                Snackbar.LENGTH_LONG
            ).setAction(resources.getString(R.string.undo)) {
                addOrder(itemPosition, orderItem)
            }.show()
        }

        updateBottomSheetText()
    }

    /*
     * Private methods
     */

    private fun addOrder(index: Int?, orderItem: OrderItem) {
        // Calculate the quantity and subtotal
        activityViewModel.totalQuantity += orderItem.quantity
        activityViewModel.subtotal += orderItem.getAmount()

        for (it in activityViewModel.orderItems) {
            if (it.item == orderItem.item &&
                it.note == orderItem.note &&
                it.extraCost == orderItem.extraCost
            ) {
                // combine the two orderItems
                it.quantity += orderItem.quantity

                // Update view
                updateBottomSheetBehavior()
                updateBottomSheetText()
                return
            }
        }

        if (index == null) {
            activityViewModel.orderItems.add(orderItem)
            activityViewModel.orderItems.sortBy { it.item.name }
        } else {
            activityViewModel.orderItems.add(index, orderItem)
            binding.bottomSheet.orderList.adapter?.notifyItemInserted(index)
        }

        // Update view
        updateBottomSheetBehavior()
        updateBottomSheetText()
    }

    private fun removeCurrentOrder() {
        activityViewModel.totalQuantity = 0
        activityViewModel.subtotal = 0f
        activityViewModel.orderItems.clear()
        activityViewModel.isTakeout = false
        activityViewModel.isPaid = false
        updateBottomSheetBehavior()
    }

    private fun updateBottomSheetBehavior() {
        if (activityViewModel.orderItems.isEmpty()) {
            // Hide bottom sheet if there is no order item
            bottomSheetBehavior.isHideable = true
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

            // Remove bottom margin when bottom sheet is hidden
            val layoutParams = binding.menuPage.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.bottomMargin = 0
            binding.menuPage.layoutParams = layoutParams
        } else {
            // Update view
            // Note the notifyDateSetChange() needs to be called after bottom sheet behaviors are set
            // Otherwise the bottom sheet will expend and collapse quickly, it doesn't look nice
            bottomSheetBehavior.isHideable = false
            adapter.notifyDataSetChanged()

            // Add bottom margin when bottom sheet is showing
            val layoutParams = binding.menuPage.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.bottomMargin =
                resources.getDimensionPixelOffset(R.dimen.bottom_sheet_peek_height)
            binding.menuPage.layoutParams = layoutParams
        }
    }

    private fun setBottomSheetText() {
        binding.bottomSheet.quantity.text = activityViewModel.totalQuantity.toString()
        binding.bottomSheet.totalAmount.text =
            currencyFormat((activityViewModel.subtotal * (1 + taxRate)))
        binding.bottomSheet.textTax.text =
            resources.getString(R.string.title_tax, (taxRate * 100).toInt().toString())
        binding.bottomSheet.tax.text = currencyFormat((activityViewModel.subtotal * taxRate))
        binding.bottomSheet.subtotal.text = currencyFormat(activityViewModel.subtotal)
    }

    private fun updateBottomSheetText() {
        // Set new display text
        setBottomSheetText()

        // Set some cool animation for text change
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
            binding.bottomSheet.quantity.startAnimation(slideDownAnimation)
            binding.bottomSheet.totalAmount.startAnimation(slideDownAnimation)
        }
    }

    private fun getTabTitle(position: Int): String? = when (position) {
        APPETIZER_PAGE_INDEX -> getString(R.string.appetizer_title)
        BREAKFAST_PAGE_INDEX -> getString(R.string.breakfast_title)
        MAIN_PAGE_INDEX -> getString(R.string.main_title)
        DRINK_PAGE_INDEX -> getString(R.string.drink_title)
        else -> null
    }

    private fun showDefaultOptionsMenu() {
        activity?.let {
            (it as MainActivity).supportActionBar?.title = getString(R.string.title_menu)
            isOptionMenuViable = false
            it.invalidateOptionsMenu()
        }
    }

    private fun showOrderDetailOptionsMenu() {
        activity?.let {
            (it as MainActivity).supportActionBar?.title =
                getString(R.string.title_current_order)
            isOptionMenuViable = true
            it.invalidateOptionsMenu()
        }
    }

    private fun showDefaultBottomSheetShape() {
        context?.let {
            binding.bottomSheet.header.background =
                ContextCompat.getDrawable(it, R.drawable.shape_bottom_sheet)
        }
    }

    private fun showExpandedBottomSheetShape() {
        context?.let {
            binding.bottomSheet.header.background =
                ContextCompat.getDrawable(it, R.color.colorPrimary)
        }
    }

    private fun toggleBottomSheetState() {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        } else if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private inner class OnBottomSheetHeaderClickListener : View.OnClickListener {
        override fun onClick(p0: View?) {
            toggleBottomSheetState()
        }
    }

    private inner class BottomSheetCallback : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {}

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                showExpandedBottomSheetShape()
                showOrderDetailOptionsMenu()
            } else {
                showDefaultBottomSheetShape()
                showDefaultOptionsMenu()
            }
        }
    }

    companion object {
        var taxRate = 0.13f
    }
}
