package ua.ideabank.obank.core.presentation.livedata

import android.os.CountDownTimer
import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData

/**
 * Mutable live data with countdown inside. Will receive value when countdown will be finished.
 *
 * @param millis - The number of millis in the future from the call
 *   to start() until the countdown is done and onFinish()
 *   is called.
 *
 * We can ignore countdown with setIgnoreValue() method
 *
 * */

open class DebounceMutableLiveData<T>(millis: Long) : MutableLiveData<T>() {
	private var newValue: T? = null
	private var timer = object : CountDownTimer(millis, Long.MAX_VALUE) {
		override fun onTick(millisUntilFinished: Long) {
			//stub
		}

		override fun onFinish() {
			setSuperValue(newValue)
		}
	}
	private var ignoreTimer = false

	@MainThread
	override fun setValue(t: T?) {
		newValue = t
		if (t is String) {
			ignoreTimer = t.isEmpty()
		}

		if (ignoreTimer) {
			setSuperValue(newValue)
		} else {
			timer.cancel()
			timer.start()
		}
	}

	@MainThread
	fun setIgnoreValue(ignoreTimer: Boolean){
		this.ignoreTimer = ignoreTimer
	}

	@MainThread
	private fun setSuperValue(t: T?) {
		super.setValue(t)
	}
}