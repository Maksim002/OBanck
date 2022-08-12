package ua.ideabank.obank.core.presentation.extention

import android.content.res.Resources
import android.util.TypedValue

fun Resources.Theme.getColor(colorAttr: Int): Int = TypedValue().apply {
	resolveAttribute(colorAttr, this, true)
}.data