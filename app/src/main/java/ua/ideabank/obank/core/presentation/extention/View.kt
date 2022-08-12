package ua.ideabank.obank.core.presentation.extention

import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.animation.addListener
import androidx.core.view.*

val View.widthWithMargins: Int get() = this.marginStart + this.width + this.marginEnd
val View.heightWithMargins: Int get() = this.marginTop + this.height + this.marginBottom

fun View.showWithAnimation() {
	if (!isVisible) {
		ObjectAnimator.ofFloat(
			this, "alpha", 0f, 1f
		).apply {
			duration = 300L
			addListener(onStart = { this@showWithAnimation.visibility = View.VISIBLE })
			start()
		}
	}
}

fun View.hideWithAnimation() {
	if (isVisible) {
		ObjectAnimator.ofFloat(
			this, "alpha", 1f, 0f
		).apply {
			duration = 300L
			addListener(onEnd = { this@hideWithAnimation.visibility = View.GONE })
			start()
		}
	}
}

fun View.show() {
	if (!this.isVisible) visibility = View.VISIBLE
}

fun View.hide(isGone: Boolean = true) {
	if (this.isVisible) visibility = if (isGone) View.GONE else View.INVISIBLE
}

fun View.requestViewSize(result: (width: Int, height: Int) -> Unit): View {
	viewTreeObserver.addOnGlobalLayoutListener(
		object : ViewTreeObserver.OnGlobalLayoutListener {

			override fun onGlobalLayout() {
				viewTreeObserver.removeOnGlobalLayoutListener(this)
				result.invoke(width, height)
			}
		}
	)

	return this
}

fun View.toBitmap(): Bitmap {
	var bitmap: Bitmap
	this.apply {
		val spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
		measure(spec, spec)
		layout(0, 0, measuredWidth, measuredHeight)
		bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
		draw(Canvas(bitmap))
	}
	return bitmap
}