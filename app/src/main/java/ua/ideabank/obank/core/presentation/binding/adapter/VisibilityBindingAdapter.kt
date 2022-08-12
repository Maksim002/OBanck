package ua.ideabank.obank.core.presentation.binding.adapter

import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.addListener
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.transition.TransitionManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ua.ideabank.obank.core.presentation.extention.hide
import ua.ideabank.obank.core.presentation.extention.show

object VisibilityBindingAdapter {

	@JvmStatic
	@BindingAdapter(value = ["android:bind_isVisibleOrGone"], requireAll = false)
	fun isVisible(view: View, isVisible: Boolean) {
		view.visibility = if (isVisible) View.VISIBLE else View.GONE
	}

	@JvmStatic
	@BindingAdapter(value = ["android:bind_isVisibleOrInvisible"], requireAll = false)
	fun isGone(view: View, isVisible: Boolean) {
		view.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
	}

	@JvmStatic
	@BindingAdapter(value = ["android:bind_isVisibleOrGoneAnimated"], requireAll = false)
	fun isVisibleAnimated(view: View, isVisible: Boolean) {
		ObjectAnimator.ofFloat(
			view, "alpha", if (isVisible) 0f else 1f,
			if (isVisible) 1f else 0f
		).apply {
			duration = 300L
			addListener(
				onStart = {
					if (isVisible && !view.isVisible) {
						view.visibility = View.VISIBLE
					}
				},
				onEnd = {
					if (!isVisible && view.isVisible) {
						view.visibility = View.GONE
					}
				}
			)
			start()
		}
	}

	@JvmStatic
	@BindingAdapter(value = ["android:bind_isVisibleOrGoneAnimatedTransition"], requireAll = false)
	fun isVisibleAnimatedTransition(view: View, isVisible: Boolean) {
		TransitionManager.beginDelayedTransition(view.parent as ViewGroup)
		if (isVisible) view.show() else view.hide(true)
	}

	@JvmStatic
	@BindingAdapter(value = ["android:bind_isShowOrHide"], requireAll = false)
	fun isVisible(view: FloatingActionButton, isVisible: Boolean) {
		if (isVisible) {
			view.show()
		} else {
			view.hide()
		}
	}
}