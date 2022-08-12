package ua.ideabank.obank.core.presentation.extention

import ua.ideabank.obank.core.common.Constant.Date.DATE_PATTERN
import java.text.SimpleDateFormat
import java.util.*

fun Date.fromDateToString(pattern: String = DATE_PATTERN): String? {
	return this.let { SimpleDateFormat(pattern, Locale.getDefault()).format(this) }
}