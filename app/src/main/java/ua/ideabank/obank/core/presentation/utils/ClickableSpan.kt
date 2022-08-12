package ua.ideabank.obank.core.presentation.utils

import android.content.res.Resources
import android.text.TextPaint
import android.util.TypedValue
import android.view.View
import ua.ideabank.obank.R

class ClickableSpan(
    theme: Resources.Theme,
    colorAttr: Int = R.color.accent,
    private val onClick: (view: View) -> Unit
) :
	android.text.style.ClickableSpan() {

	private val textColor: Int = TypedValue().apply {
		theme.resolveAttribute(colorAttr, this, true)
	}.data

	override fun onClick(widget: View) = onClick.invoke(widget)

	override fun updateDrawState(ds: TextPaint) {
		ds.color = textColor
		ds.isUnderlineText = false
	}
}