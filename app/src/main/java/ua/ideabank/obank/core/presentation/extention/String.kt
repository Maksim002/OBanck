package ua.ideabank.obank.core.presentation.extention

import ua.ideabank.obank.core.common.Constant
import java.text.SimpleDateFormat
import java.util.*

fun String.fromStringToDate(pattern: String = Constant.Date.DATE_PATTERN): Date? {
	return this.let { SimpleDateFormat(pattern, Locale.getDefault()).parse(this) }
}