package ua.ideabank.obank.data.local.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.runBlocking
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@Suppress("unused")
class SharedPrefsHelper(var sharedPreferences: SharedPreferences) {
	val all: Map<String, *>
		get() = sharedPreferences.all

	private val editor: SharedPreferences.Editor
		get() = sharedPreferences.edit()

	fun getInt(key: String, defValue: Int = 0): Int {
		return sharedPreferences.getInt(key, defValue)
	}

	fun getBoolean(key: String, defValue: Boolean = false): Boolean {
		return sharedPreferences.getBoolean(key, defValue)
	}

	fun getLong(key: String, defValue: Long = 0L): Long {
		return sharedPreferences.getLong(key, defValue)
	}

	fun getDouble(key: String, defValue: Double = 0.0): Double {
		return java.lang.Double.longBitsToDouble(
			sharedPreferences.getLong(
				key,
				java.lang.Double.doubleToLongBits(defValue)
			)
		)
	}

	fun getFloat(key: String, defValue: Float = 0F): Float {
		return sharedPreferences.getFloat(key, defValue)
	}

	fun getString(key: String, defValue: String = ""): String {
		return sharedPreferences.getString(key, defValue) ?: defValue
	}

	fun putLong(key: String, value: Long) {
		commit { putLong(key, value) }
	}

	fun putInt(key: String, value: Int) {
		commit { putInt(key, value) }
	}

	fun putDouble(key: String, value: Double) {
		commit { putLong(key, java.lang.Double.doubleToRawLongBits(value)) }
	}

	fun putFloat(key: String, value: Float) {
		commit { putFloat(key, value) }
	}

	fun putBoolean(key: String, value: Boolean) {
		commit { putBoolean(key, value) }
	}

	fun putString(key: String, value: String?) {
		commit {
			if (value.isNullOrEmpty()) {
				remove(key)
			} else {
				putString(key, value)
			}
		}
	}

	fun remove(key: String) {
		commit { remove(key) }
	}

	fun contains(key: String): Boolean {
		return sharedPreferences.contains(key)
	}

	fun clear() {
		commit { clear() }
	}

	fun boolean(key: String, defValue: Boolean = false): ReadWriteProperty<Any?, Boolean> =
		BooleanDelegate(key, defValue)

	fun string(key: String, defValue: String = ""): ReadWriteProperty<Any?, String> =
		StringDelegate(key, defValue)

	fun int(key: String, defValue: Int = 0): ReadWriteProperty<Any?, Int> =
		IntDelegate(key, defValue)

	fun long(key: String, defValue: Long = 0L): ReadWriteProperty<Any?, Long> =
		LongDelegate(key, defValue)

	fun float(key: String, defValue: Float = 0F): ReadWriteProperty<Any?, Float> =
		FloatDelegate(key, defValue)

	fun double(key: String, defValue: Double = 0.0): ReadWriteProperty<Any?, Double> =
		DoubleDelegate(key, defValue)


	@SuppressLint("ApplySharedPref")
	inline fun commit(block: SharedPreferences.Editor.() -> Unit) {
		sharedPreferences
			.edit()
			.apply(block)
			.commit()
	}

	/**
	 * Adds [SharedPreferences.OnSharedPreferenceChangeListener] to [SharedPreferences]
	 * which listens to changes into it.
	 *
	 * @param key key to observe.
	 * @param onChanged invokes when [SharedPreferences.OnSharedPreferenceChangeListener.onSharedPreferenceChanged] method is called.
	 * Should returns as a result a refreshed data from database which will be offered to [callbackFlow].
	 *
	 * @return [Flow] of changed data.
	 */
	@ExperimentalCoroutinesApi
	inline fun <R> observeChange(
		key: String,
		crossinline onChanged: suspend () -> R
	): Flow<R> {
		Log.d(this@SharedPrefsHelper.javaClass.simpleName, "observeChange: ")

		return callbackFlow {

			val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, changedKey ->
				runBlocking {
					if (key != changedKey) return@runBlocking

					if (!isClosedForSend)
						offer(onChanged())
				}
			}

			if (!isClosedForSend) {
				offer(onChanged())
			}

			sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

			awaitClose {
				sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
			}
		}
	}


	private inner class BooleanDelegate(private val key: String, private val defValue: Boolean = false) :
		ReadWriteProperty<Any?, Boolean> {

		override fun getValue(thisRef: Any?, property: KProperty<*>): Boolean = getBoolean(key, defValue)
		override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) = putBoolean(key, value)
	}

	private inner class StringDelegate(private val key: String, private val defValue: String = "") :
		ReadWriteProperty<Any?, String> {

		override fun getValue(thisRef: Any?, property: KProperty<*>): String = getString(key, defValue)
		override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) = putString(key, value)
	}

	private inner class IntDelegate(private val key: String, private val defValue: Int = 0) :
		ReadWriteProperty<Any?, Int> {

		override fun getValue(thisRef: Any?, property: KProperty<*>): Int = getInt(key, defValue)
		override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) = putInt(key, value)
	}

	private inner class LongDelegate(private val key: String, private val defValue: Long = 0) :
		ReadWriteProperty<Any?, Long> {

		override fun getValue(thisRef: Any?, property: KProperty<*>): Long = getLong(key, defValue)
		override fun setValue(thisRef: Any?, property: KProperty<*>, value: Long) = putLong(key, value)
	}

	private inner class FloatDelegate(private val key: String, private val defValue: Float = 0F) :
		ReadWriteProperty<Any?, Float> {

		override fun getValue(thisRef: Any?, property: KProperty<*>): Float = getFloat(key, defValue)
		override fun setValue(thisRef: Any?, property: KProperty<*>, value: Float) = putFloat(key, value)
	}

	private inner class DoubleDelegate(private val key: String, private val defValue: Double = 0.0) :
		ReadWriteProperty<Any?, Double> {

		override fun getValue(thisRef: Any?, property: KProperty<*>): Double = getDouble(key, defValue)
		override fun setValue(thisRef: Any?, property: KProperty<*>, value: Double) = putDouble(key, value)
	}

}

