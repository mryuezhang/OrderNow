package com.yue.ordernow.utils

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern


fun currencyFormat(rawInput: Float): String = String.format("$%.2f", rawInput)

class CurrencyFormatInputFilter : InputFilter {

    private val currencyPattern = Pattern.compile("(0|[1-9]+[0-9]*)?(\\.[0-9]{0,2})?")

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {

        val result = (dest.subSequence(0, dstart).toString()
                + source.toString()
                + dest.subSequence(dend, dest.length))

        val matcher = currencyPattern.matcher(result)

        return if (!matcher.matches()) "$" + dest.subSequence(dstart, dend) else null

    }
}