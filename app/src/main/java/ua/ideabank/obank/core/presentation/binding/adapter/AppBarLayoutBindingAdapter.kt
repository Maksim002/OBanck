package ua.ideabank.obank.core.presentation.binding.adapter

import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout


object AppBarLayoutBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["android:bind_isElevationVisible"], requireAll = false)
    fun isVisible(view: AppBarLayout, isVisible: Boolean) {
        if (isVisible) {
            view.elevation = 5f
        } else {
            view.elevation = 0f
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["android:bind_isAppbarExpand"], requireAll = false)
    fun isExpand(view: AppBarLayout, isExpand: Boolean) {
        view.setExpanded(isExpand)
    }

    @JvmStatic
    @BindingAdapter(value = ["android:set_scroll_flags"], requireAll = false)
    fun setCollapsingToolbarScroll(collapsingToolbar: Toolbar, withFlags: Boolean) {
        if (withFlags) {
            val toolbarLayoutParams = collapsingToolbar.layoutParams as AppBarLayout.LayoutParams
            toolbarLayoutParams.scrollFlags = (AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                    or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                    or AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP)
            collapsingToolbar.layoutParams = toolbarLayoutParams
        } else {
            val toolbarLayoutParams = collapsingToolbar.layoutParams as AppBarLayout.LayoutParams
            toolbarLayoutParams.scrollFlags = 0
            collapsingToolbar.layoutParams = toolbarLayoutParams
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["android:bind_title"], requireAll = false)
    fun setTitle(view: CollapsingToolbarLayout, title: String) {
        view.title = title
    }

    @JvmStatic
    @BindingAdapter(value = ["app:bind_color"], requireAll = false)
    fun setColor(view: CollapsingToolbarLayout, color: Int) {
        view.setContentScrimColor(color)
    }
}