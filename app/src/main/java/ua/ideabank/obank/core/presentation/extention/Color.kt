package ua.ideabank.obank.core.presentation.extention

import android.graphics.Color
import androidx.annotation.ColorInt

@get:ColorInt
inline val Int.transparent: Int
	get() = Color.argb(
		0,
		Color.red(this),
		Color.green(this),
		Color.blue(this)
	)