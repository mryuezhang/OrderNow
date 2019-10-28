package com.yue.ordernow.fragments

import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.yue.ordernow.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val taxRatePreference: EditTextPreference? =
            findPreference(resources.getString(R.string.key_tax_rate))
        taxRatePreference?.text = PreferenceManager.getDefaultSharedPreferences(context).getString(
            resources.getString(R.string.key_tax_rate),
            resources.getString(R.string.default_tax_rate)
        )
        taxRatePreference?.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_NUMBER
            editText.setSelection(editText.text.length)
        }
        taxRatePreference?.summaryProvider =
            Preference.SummaryProvider<EditTextPreference> { preference ->
                val text = preference.text
                if (TextUtils.isEmpty(text)) {
                    "Not set"
                } else {
                    "$text%"
                }
            }
    }

}