package ua.ideabank.obank.core.presentation.binding.adapter

import android.widget.Button
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.adapters.ListenerUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton

object BottomViewBindingAdapter {

    private var currentItemId: Int? = null

    @JvmStatic
    @BindingAdapter(value = ["android:bind_bnv_currentItem"])
    fun setCurrentItem(view: BottomNavigationView, itemId: Int) {
        if(view.selectedItemId != itemId){
            view.selectedItemId = itemId
            currentItemId = itemId
        }
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "android:bind_bnv_currentItem")
    fun getCurrentItem(view: BottomNavigationView): Int {
        return if(currentItemId != view.selectedItemId) view.selectedItemId else 0
    }

    @JvmStatic
    @BindingAdapter("android:bind_onNavigationItemSelected")
    fun setOnNavigationItemSelected(
        view: BottomNavigationView, listener: BottomNavigationView.OnNavigationItemSelectedListener?
    ) {
        view.setOnNavigationItemSelectedListener(listener)
    }

    @JvmStatic
    @BindingAdapter(value = ["android:bind_bnv_currentItemAttrChanged"])
    fun setOnNavigationItemSelectedListener(
        view: BottomNavigationView,
        listener: InverseBindingListener?
    ) {
        var newListener: BottomNavigationView.OnNavigationItemSelectedListener? = null
        if (listener != null) {
            newListener = BottomNavigationView.OnNavigationItemSelectedListener {
                listener.onChange()
                true
            }
        }

        val oldListener = ListenerUtil.trackListener(view, newListener, view.id)
        oldListener?.let { view.setOnNavigationItemSelectedListener(null) }
        newListener?.let { view.setOnNavigationItemSelectedListener(newListener) }
    }

    @JvmStatic
    @BindingAdapter(
        value = [
            "android:bind_bnv_badgeItemId",
            "android:bind_bnv_badgeCount",
            "android:bind_bnv_badgeVisibility"
        ], requireAll = false
    )
    fun setBadge(
        view: BottomNavigationView,
        itemId: Int,
        count: Int,
        visibility: Boolean
    ) {

        view.getOrCreateBadge(itemId).apply {
            number = count
            maxCharacterCount = 2
        }
        if (!visibility) view.removeBadge(itemId)

    }

    @JvmStatic
    @BindingAdapter(value = ["android:bind_btn"], requireAll = false)
    fun setBtn(view: MaterialButton, string: String) {
        view.text = string
    }
}