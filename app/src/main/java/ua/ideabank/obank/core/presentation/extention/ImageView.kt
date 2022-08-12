package ua.ideabank.obank.core.presentation.extention

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadFromDrawable(context: Context, name: String) {
	Glide
		.with(this)
		.load(
			context.resources.getIdentifier(
				name,
				"drawable",
				context.packageName
			)
		)
		.into(this)
}