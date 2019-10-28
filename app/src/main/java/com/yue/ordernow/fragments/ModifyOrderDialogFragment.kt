package com.yue.ordernow.fragments

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.yue.ordernow.R
import com.yue.ordernow.data.OrderItem
import com.yue.ordernow.databinding.DialogAddNoteBinding
import com.yue.ordernow.utilities.CurrencyFormatInputFilter

class ModifyOrderDialogFragment(
    private val listener: ModifyOrderDialogListener,
    private val orderItem: OrderItem,
    private val position: Int
) : DialogFragment() {

    interface ModifyOrderDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, position: Int)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { it ->
            val builder = AlertDialog.Builder(it)

            // Bind the view
            val binding = DialogAddNoteBinding.inflate(it.layoutInflater)

            // Set up the title
            binding.title.text = getString(R.string.title_modify_order, orderItem.item.name)

            // Display note if there is one
            if (orderItem.note.isNotEmpty()) {
                binding.note.text = Editable.Factory.getInstance().newEditable(orderItem.note)
            }

            // Max length has to be set here since the xml attribute is overridden by filters
            binding.extraCost.filters =
                arrayOf(CurrencyFormatInputFilter(), InputFilter.LengthFilter(7))

            // Display extra cost if there is one
            if (orderItem.extraCost != 0f) {
                binding.extraCost.text =
                    Editable.Factory.getInstance().newEditable(orderItem.extraCost.toString())
            }

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

            // Display quantity
            binding.quantity.text =
                Editable.Factory.getInstance().newEditable(orderItem.quantity.toString())

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(binding.root)
                .setPositiveButton(R.string.confirm) { _, _ ->
                    if (!binding.note.text.isNullOrBlank()) {
                        orderItem.note = binding.note.text.toString()
                    }
                    orderItem.extraCost = if (binding.extraCost.text!!.isNotEmpty()) {
                        binding.extraCost.text.toString().toFloat()
                    } else {
                        0f
                    }
                    orderItem.quantity = binding.quantity.text.toString().toInt()

                    // Pass the data to the listener
                    listener.onDialogPositiveClick(this, position)
                }
                .setNegativeButton(R.string.cancel) { _, _ ->
                    dialog?.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        const val TAG = "ModifyOrderDialogFragment"
    }
}