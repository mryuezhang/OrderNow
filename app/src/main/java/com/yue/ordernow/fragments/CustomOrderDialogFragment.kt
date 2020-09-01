package com.yue.ordernow.fragments

import android.app.Dialog
import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yue.ordernow.R
import com.yue.ordernow.data.MenuItem
import com.yue.ordernow.data.OrderItem
import com.yue.ordernow.databinding.DialogAddNoteBinding
import com.yue.ordernow.utilities.CurrencyFormatInputFilter


class CustomOrderDialogFragment(
    private val listener: CustomOrderDialogListener,
    private val menuItem: MenuItem
) : DialogFragment() {

    interface CustomOrderDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, orderItem: OrderItem)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { it ->
            val builder = MaterialAlertDialogBuilder(it)

            // Bind the view
            val binding = DialogAddNoteBinding.inflate(it.layoutInflater)

            // Set up the title
            binding.title.text = getString(R.string.title_custom_order, menuItem.name)

            // Max length has to be set here since the xml attribute is overridden by filters
            binding.extraCost.filters =
                arrayOf(CurrencyFormatInputFilter(), InputFilter.LengthFilter(7))

            // Add error check and error message for user-input quantity
            binding.quantity.setOnFocusChangeListener { _, _ ->
                when (binding.quantity.text.toString()) {
                    "" -> binding.textInputLayoutQuantity.error =
                        resources.getString(R.string.error_quantity_cannot_be_null)
                    "0" -> binding.textInputLayoutQuantity.error =
                        resources.getString(R.string.error_quantity_cannot_be_zero)
                    else -> binding.textInputLayoutQuantity.error = ""
                }
            }

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(binding.root)
                .setPositiveButton(R.string.place_order) { _, _ ->
                    // Create the order item based on user inputs
                    val orderItem =
                        OrderItem(menuItem, binding.quantity.text.toString().toInt(), "").apply {
                            if (binding.note.text!!.isNotBlank() && binding.note.text!!.isNotEmpty()) {
                                note = binding.note.text.toString()
                            }
                            if (binding.extraCost.text!!.isNotEmpty()) {
                                this.extraCost = binding.extraCost.text.toString().toFloat()
                            }
                        }
                    // Pass the data to the listener
                    listener.onDialogPositiveClick(this, orderItem)
                }
                .setNegativeButton(R.string.cancel) { _, _ ->
                    dialog?.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        const val TAG = "CustomOrderDialogFragment"
    }
}