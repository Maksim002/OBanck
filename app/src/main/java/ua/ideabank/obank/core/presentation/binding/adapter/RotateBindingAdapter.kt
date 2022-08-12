package ua.ideabank.obank.core.presentation.binding.adapter

import android.view.View
import androidx.databinding.BindingAdapter

object RotateBindingAdapter {

	@JvmStatic
	@BindingAdapter(value = ["android:bind_rotate_x180"], requireAll = false)
	fun rotateX180(view: View, isVisible: Boolean) {
		view.animate().rotation(if(isVisible) 180.0f else 0.0f)
	}
}