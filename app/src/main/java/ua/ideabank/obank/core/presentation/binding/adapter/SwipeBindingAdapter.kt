package ua.ideabank.obank.core.presentation.binding.adapter

import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

object SwipeBindingAdapter {

	@JvmStatic
	@BindingAdapter(value = ["android:bind_srl_onRefresh"], requireAll = false)
	fun onRefresh(swipe: SwipeRefreshLayout, listener: SwipeRefreshLayout.OnRefreshListener) {
		swipe.setOnRefreshListener(listener)
	}

	@JvmStatic
	@BindingAdapter(value = ["android:bind_srl_enabled"], requireAll = false)
	fun enable(swipe: SwipeRefreshLayout, enable: Boolean) {
		swipe.isEnabled = enable
	}

	@JvmStatic
	@BindingAdapter(value = ["android:bind_srl_refreshing"], requireAll = false)
	fun refreshing(swipe: SwipeRefreshLayout, refreshing: Boolean) {
		if(swipe.isEnabled) swipe.isRefreshing = refreshing
	}
}