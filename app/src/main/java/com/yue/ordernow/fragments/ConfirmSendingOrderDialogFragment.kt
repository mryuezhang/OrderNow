package com.yue.ordernow.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.yue.ordernow.R
import com.yue.ordernow.databinding.DialogConfrimSendingOrderBinding

class ConfirmSendingOrderDialogFragment(private val listener: ConfirmSendingOrderDialogFragmentListener) :
    DialogFragment() {

    interface ConfirmSendingOrderDialogFragmentListener {
        fun onDialogPositiveClick(isPaid: Boolean)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { it ->
            val builder = AlertDialog.Builder(it)

            // Bind the view
            val binding = DialogConfrimSendingOrderBinding.inflate(it.layoutInflater)

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(binding.root)
                .setPositiveButton(R.string.action_send_orders) { _, _ ->
                    listener.onDialogPositiveClick(binding.radioGroup.checkedRadioButtonId == R.id.paid)
                }
                .setNegativeButton(R.string.cancel) { _, _ ->
                    dialog?.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        const val TAG = "ConfirmSendingOrderDialogFragment"
    }
}