package ua.ideabank.obank.core.presentation.binding.adapter

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*

object DateTextViewBindingAdapter {

	@JvmStatic
	@BindingAdapter(value = ["android:bind_data_tv", "android:bind_pattern"])
	fun setTime(view: TextView, data: Date?, pattern: String?) {
		@SuppressLint("SimpleDateFormat")
		if(data!=null && pattern!=null){
			val dateTimeFormatter = SimpleDateFormat(pattern)
			view.text = dateTimeFormatter.format(data)
		}
	}

	@JvmStatic
	@BindingAdapter("android:bind_textView")
	fun setSrcVector(imageView: ImageView, @DrawableRes drawable: Int) {
		imageView.setImageResource(drawable)
	}
}