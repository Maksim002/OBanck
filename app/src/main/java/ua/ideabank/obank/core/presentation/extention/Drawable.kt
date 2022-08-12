package ua.ideabank.obank.core.presentation.extention

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

fun Drawable.toBitmap(): Bitmap? {
	var bitmap: Bitmap? = null
	if (this is BitmapDrawable) {
		if (bitmap != null) {
			return bitmap
		}
	}
	bitmap = if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
		Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
	} else {
		Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
	}
	val canvas = Canvas(bitmap)
	setBounds(0, 0, canvas.width, canvas.height)
	draw(canvas)
	return bitmap
}
