package ua.ideabank.obank.core.presentation.extention

import androidx.annotation.MainThread
import androidx.lifecycle.*
import ua.ideabank.obank.core.presentation.utils.OneTimeAction

inline val <T> LiveData<T>.requireValue: T
	get() = value ?: throw IllegalStateException("LiveData's value must not be null!")

/**
 * Observes only non-null [LiveData] values.
 */
@MainThread
inline fun <T> LiveData<T>.observe(owner: LifecycleOwner, crossinline observer: (t: T) -> Unit) {
	observe(owner, Observer { it.let(observer) })
}

@MainThread
inline fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, crossinline observer: (T) -> Unit) {
	observe(owner, object : Observer<T> {

		override fun onChanged(t: T) {
			removeObserver(this)
			observer(t)
		}
	})
}

@MainThread
inline fun <R> mediatorLiveData(
	vararg sources: LiveData<out Any>,
	crossinline onChanged: () -> R
): MutableLiveData<R> = MediatorLiveData<R>().apply {
	sources.forEach { source: LiveData<out Any> ->
		addSource(source) { value = onChanged() }
	}
}

/**
 * Observe array of LiveDatas when value of one of their has changed and invoke it in action.
 *
 * Use it if you need observe 4 or more LiveDatas.
 */
@MainThread
fun observeArgForever(
	vararg array: LiveData<out Any>,
	block: (Array<Any>) -> Unit
) {
	val action = OneTimeAction<Array<Any?>> {
		if (!it.contains(null)) block(it.requireNoNulls())
	}
	array.forEach { liveData ->
		liveData.observeForever {
			val newArray = arrayOfNulls<Any>(array.size)
			array.forEachIndexed { index, liveData ->
				newArray[index] = liveData.value
			}
			action.invoke(newArray)
		}
	}
}

@MainThread
fun <T> MutableLiveData<T>.notify() {
	this.value = this.value
}

@MainThread
fun <T> MutableLiveData<T>.notifyPost() {
	this.postValue(this.value)
}


fun <T> LiveData<T>.distinctNotNullValues(onDistinct: (current: T, new: T) -> Unit = { _, _ -> }): LiveData<T?> =
	object : MediatorLiveData<T?>() {
		private var currentDistinctValue: T? = null

		init {
			this@distinctNotNullValues.value?.apply { currentDistinctValue = this }
			addSource(this@distinctNotNullValues) { newValue ->
				currentDistinctValue.let { distinctValue ->
					if (distinctValue == null && newValue != null) {
						currentDistinctValue = newValue
					}
					if (distinctValue != null && newValue != null && distinctValue != newValue) {
						value = newValue
						onDistinct(distinctValue, newValue)
						currentDistinctValue = newValue
					}
				}
			}
		}
	}