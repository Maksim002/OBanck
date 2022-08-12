package ua.ideabank.obank.core.presentation.livedata

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Sends only new updates after subscription
 *
 * This avoids a common problem with events: on configuration change (like rotation) an update
 * can be emitted if the innerObserver is active. This LiveData only calls the observable if there's an
 * explicit call to setValue() or call().
 */
class SingleLiveEvent<T> : MutableLiveData<T>() {

	private val pending = AtomicBoolean(false)
	private val pendingGet = AtomicBoolean(false)

	@MainThread
	override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
		super.observe(owner, {
			if (pending.compareAndSet(true, false)) {
				observer.onChanged(it)
			}
		})
	}

	/**
	 * Used for cases where T is variable.
	 */
	@MainThread
	override fun setValue(t: T?) {
		pending.set(true)
		pendingGet.set(true)
		super.setValue(t)
	}

	/**
	 * Used for cases where T is variable.
	 */
	@MainThread
	override fun postValue(t: T?) {
		pending.set(true)
		pendingGet.set(true)
		super.postValue(t)
	}

	override fun getValue(): T? {
		return if (pendingGet.compareAndSet(true, false)) super.getValue() else null
	}

	/**
	 * Used for cases where T is Void, to make calls cleaner.
	 */
	@MainThread
	fun call() {
		value = null
	}
}