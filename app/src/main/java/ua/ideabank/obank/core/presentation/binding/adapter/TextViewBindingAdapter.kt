package ua.ideabank.obank.core.presentation.binding.adapter

import android.content.ClipData
import android.graphics.Color
import android.widget.TextView
import androidx.databinding.BindingAdapter

object TextViewBindingAdapter {
    @JvmStatic
    @BindingAdapter(value = ["android:bind_text"], requireAll = false)
    fun setText(view: TextView, text: String) {
        view.text = text
    }

    @JvmStatic
    @BindingAdapter(value = ["android:bind_color"], requireAll = false)
    fun setColor(view: TextView, color: Int) {
        view.setTextColor(color);
    }
}