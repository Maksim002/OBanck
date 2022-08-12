package ua.ideabank.obank.core.presentation.extention

import android.graphics.Color
import android.widget.TextView
import com.klinker.android.link_builder.Link
import com.klinker.android.link_builder.applyLinks

fun TextView.stringToLink(
    text: String,
    link: String,
    listener: OnLinkClickListener? = null,
    color: Int? = null
) {
    val result = Link(link)
        .setTextColor(color ?: Color.parseColor("#BE2987"))
        .setOnClickListener {
            listener!!.onClick()
        }
        .setBold(true)
        .setUnderlined(false)
    this.text = text
    this.applyLinks(result)
}

interface OnLinkClickListener {
    fun onClick()
}