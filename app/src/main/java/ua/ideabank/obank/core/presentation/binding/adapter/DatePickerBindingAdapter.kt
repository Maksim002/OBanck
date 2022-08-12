package ua.ideabank.obank.core.presentation.binding.adapter

import android.annotation.SuppressLint
import android.widget.DatePicker
import androidx.databinding.BindingAdapter
import java.util.*

object DatePickerBindingAdapter {
	@SuppressLint("CheckResult")
	@JvmStatic
	@BindingAdapter(value = ["android:bind_date", "android:bind_onDateChanged"], requireAll = false)
	fun setDate(
		view: DatePicker, date: Date?, listener: DatePicker.OnDateChangedListener?
	) {
		val calendar = Calendar.getInstance()
		view.maxDate = calendar.timeInMillis
		if (date != null) calendar.timeInMillis = date.time
		val year = calendar.get(Calendar.YEAR)
		val month = calendar.get(Calendar.MONTH)
		val day = calendar.get(Calendar.DAY_OF_MONTH)
		view.init(year, month, day, listener)
	}
}