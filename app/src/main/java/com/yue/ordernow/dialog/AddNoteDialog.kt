package com.yue.ordernow.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.yue.ordernow.R
import com.yue.ordernow.data.MenuItem
import com.yue.ordernow.data.OrderItem
import com.yue.ordernow.utils.CurrencyFormatInputFilter

class AddNoteDialog(private val menuItem: MenuItem) : DialogFragment() {

    // Use this instance of the interface to deliver action events
    private lateinit var listener: AddNoteDialogListener

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    interface AddNoteDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, orderItem: OrderItem)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as AddNoteDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                (context.toString() +
                        " must implement NoticeDialogListener")
            )
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { it ->
            val builder = AlertDialog.Builder(it)
            val view = requireActivity().layoutInflater.inflate(R.layout.dialog_add_note, null)
            val title: TextView = view.findViewById(R.id.add_note_title)
            val inputNote: TextInputEditText = view.findViewById(R.id.input_note)
            val extraCost: TextInputEditText = view.findViewById(R.id.extra_cost)
            val quantityLayout: TextInputLayout = view.findViewById(R.id.textInputLayout3)
            val quantity: TextInputEditText = view.findViewById(R.id.quantity)

            title.text = getString(R.string.title_add_note, menuItem.name)

            // max length has to be set here since the xml attribute is overridden by filters
            extraCost.filters = arrayOf(CurrencyFormatInputFilter(), InputFilter.LengthFilter(7))

            // add error check and error message for user-input quantity
            quantity.setOnFocusChangeListener { _, _ ->
                when (quantity.text.toString()) {
                    "" -> quantityLayout.error = "quantity cannot be null"
                    "0" -> quantityLayout.error = "quantity cannot be 0"
                    else -> quantityLayout.error = ""
                }
            }

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(view)
                .setPositiveButton(R.string.place_order) { _, _ ->
                    if (extraCost.text!!.isNotEmpty()) {
                        menuItem.price += extraCost.text.toString().toFloat()
                    }

                    if (inputNote.text!!.isNotBlank() && inputNote.text!!.isNotEmpty()) {
                        listener.onDialogPositiveClick(
                            this,
                            OrderItem(
                                menuItem,
                                quantity.text.toString().toInt(),
                                inputNote.text.toString()
                            )
                        )
                    } else {
                        listener.onDialogPositiveClick(
                            this,
                            OrderItem(menuItem, quantity.text.toString().toInt(), "")
                        )
                    }
                }
                .setNegativeButton(R.string.cancel) { _, _ ->
                    dialog?.cancel()
                }

            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }

}