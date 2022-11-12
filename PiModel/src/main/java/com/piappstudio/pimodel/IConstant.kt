/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.pimodel

import android.icu.util.Currency
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object Constant {
    const val EMPTY_STRING = ""

    object PiFormat {
        val month = SimpleDateFormat("MMM", Locale.US)
        val orderServer= SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.US)
        val orderDisplay = SimpleDateFormat("MMMM dd, yyyy", Locale.US)
        val orderTime = SimpleDateFormat("hh:mm:a", Locale.US)
        val orderItemDisplay = SimpleDateFormat("MMMM dd, yyyy hh:mm a", Locale.US)
        val mediaDateTimeFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
        val eventInputFormat = SimpleDateFormat("ddmmyyyy", Locale.US)
    }
}

fun Double.toCurrency(): String {
    val currency = Currency.getInstance(Locale.getDefault())
    val format = NumberFormat.getInstance(Locale.getDefault())
    val value = format.format(String.format("%.2f", this).toDouble())

    return "${currency.symbol} $value"
}