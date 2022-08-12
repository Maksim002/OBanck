package ua.ideabank.obank.core.common.extension

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.roundToInt

private const val decimalPatternWithCoins = "#,###,##0.00"
private const val decimalPatternWithoutCoins = "#,###,###"

fun Number?.toFormattedString(shouldShowCoins: Boolean = true, defaultValue: String = ""): String {
	this ?: return defaultValue

	val otherSymbols = DecimalFormatSymbols(Locale.getDefault()).apply {
		decimalSeparator = ','
		groupingSeparator = ' '
	}

	val decimalPattern = if (shouldShowCoins) decimalPatternWithCoins else decimalPatternWithoutCoins

	return DecimalFormat(decimalPattern, otherSymbols)
		.format(this).toString()
}

fun Float.isInteger() = (this.roundToInt().toFloat() != this)