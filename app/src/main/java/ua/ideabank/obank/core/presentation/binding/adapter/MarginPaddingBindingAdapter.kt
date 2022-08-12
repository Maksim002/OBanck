package ua.ideabank.obank.core.presentation.binding.adapter

import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.updateMargins
import androidx.core.view.updatePadding
import androidx.databinding.BindingAdapter


object MarginPaddingBindingAdapter {

	@JvmStatic
	@BindingAdapter(
		value = ["android:bind_marginStart", "android:bind_marginBottom", "android:bind_marginTop", "android:bind_marginEnd"],
		requireAll = false
	)
	fun setMargin(view: View, startMargin: Int, bottomMargin: Int, topMargin: Int, endMargin: Int) {
		val layoutParams = view.layoutParams as MarginLayoutParams
		layoutParams.setMargins(
			layoutParams.leftMargin + startMargin, layoutParams.topMargin + topMargin,
			layoutParams.rightMargin + endMargin, bottomMargin
		)
		view.layoutParams = layoutParams
	}

	@JvmStatic
	@BindingAdapter(
		value = ["android:bind_paddingStart", "android:bind_paddingBottom", "android:bind_paddingTop", "android:bind_paddingEnd"],
		requireAll = false
	)
	fun setPadding(
		view: View,
		startPadding: Int,
		bottomPadding: Int,
		topPadding: Int,
		endPadding: Int
	) {
		view.updatePadding(
			left = view.paddingLeft + startPadding,
			bottom = view.paddingBottom + bottomPadding,
			top = view.paddingTop + topPadding,
			right = view.paddingRight + endPadding
		)
	}
}