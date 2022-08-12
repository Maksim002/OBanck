package ua.ideabank.obank.core.presentation.ui.base

import android.content.Context
import android.content.res.Resources
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ua.ideabank.obank.core.presentation.livedata.dispose.DisposableContainer
import ua.ideabank.obank.core.presentation.livedata.dispose.ObserverImpl
import ua.ideabank.obank.core.presentation.utils.OneTimeAction
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import ua.ideabank.obank.BuildConfig
import ua.ideabank.obank.core.common.exception.ApiException
import ua.ideabank.obank.core.common.exception.ApiException.ErrorCode.BAD_REQUEST
import ua.ideabank.obank.core.common.exception.ApiException.ErrorCode.CONFLICT
import ua.ideabank.obank.core.common.exception.ApiException.ErrorCode.LOGOUT_CODES
import ua.ideabank.obank.core.common.exception.ApiException.ErrorCode.SERVER_ERRORS
import ua.ideabank.obank.core.common.exception.ApiException.ErrorCode.UNKNOWN
import ua.ideabank.obank.core.presentation.error.*
import ua.ideabank.obank.core.presentation.livedata.SingleLiveEvent
import ua.ideabank.obank.core.presentation.livedata.dispose.Disposable
import java.lang.ref.WeakReference
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

@Suppress("MemberVisibilityCanBePrivate", "unused")
abstract class BaseViewModel : ViewModel() {

	val baseContext = MutableLiveData<Context>()
	val theme = MutableLiveData<Resources.Theme>()
	val errorEvent = SingleLiveEvent<UiError>()
	val loadingState = MutableLiveData(false)
	val hasConnection = MutableLiveData<Boolean>()

	val showServerErrorEvent = SingleLiveEvent<Unit>()
	val showConnectionErrorEvent = SingleLiveEvent<Unit>()
	val conflictErrorEvent = SingleLiveEvent<Unit>()

	protected val disposables = DisposableContainer()

	override fun onCleared() {
		super.onCleared()

		disposables.disposeAll()
	}

	fun <T> launch(
		result: MutableLiveData<T>? = null,
		errorBlock: ((Throwable) -> Unit)? = null,
		context: CoroutineContext = Dispatchers.IO,
		scope: CoroutineScope = viewModelScope,
		loading: MutableLiveData<Boolean>? = loadingState,
		error: MutableLiveData<UiError>? = errorEvent,
		errorThrowable: MutableLiveData<Throwable>? = null,
		request: suspend CoroutineScope.() -> T
	): Job {
		return scope.launch {
			try {
				loading?.value = true
				withContext(context) {
					request()
				}.apply {
					this?.let {
						result?.value = it
					}
				}
			} catch (e: Throwable) {
				loading?.value = false
				handleError(e, error)
				errorThrowable?.value = e
				errorBlock?.invoke(e)
			} finally {
				loading?.value = false
			}
		}
	}

	protected fun <T> launchFlow(
		result: MutableLiveData<T>? = null,
		context: CoroutineContext = Dispatchers.IO,
		scope: CoroutineScope = viewModelScope,
		loading: MutableLiveData<Boolean>? = loadingState,
		error: MutableLiveData<UiError>? = errorEvent,
		request: () -> Flow<T>
	): Job {
		return request.invoke()
			.flowOn(context)
			.onStart { loading?.value = true }
			.onEach { result?.postValue(it) }
			.onCompletion { loading?.value = false }
			.catch { withContext(Dispatchers.Main) { handleError(it, error) } }
			.launchIn(scope)
	}

	protected open fun handleError(
		cause: Throwable?,
		errorLiveData: MutableLiveData<UiError>? = null
	) {
		cause ?: return
		if (BuildConfig.DEBUG) {
			cause.printStackTrace()
		}
		val info: UiError = when (cause) {
			is UnknownHostException,
			is SocketTimeoutException,
			is ConnectException -> {
				NetworkConnectionError()
			}
			is ApiException -> {
				when (cause.httpCode) {
					in LOGOUT_CODES -> ForceLogoutError(cause.details, cause)
					in SERVER_ERRORS -> ServerInternalError(cause.details, cause)
					UNKNOWN -> NetworkConnectionError()
					else -> ApiError(cause.details, cause)
				}
			}
			else -> {
				UndefinedError(cause.message.orEmpty(), cause)
			}
		}
		if (info is NetworkConnectionError){
			showConnectionErrorEvent.call()
		}
		if (info is ForceLogoutError) {
//			event.call()
		}
		if (info is ServerInternalError) {
			showServerErrorEvent.call()
		}
		if (info is ApiError) {
			if (info.cause.httpCode == UNKNOWN) {
				when (hasConnection.value) {
					true -> showServerErrorEvent.call()
					else -> showConnectionErrorEvent.call()
				}
			}
			if (info.cause.httpCode == BAD_REQUEST) {
				errorLiveData?.value = info
			}
			if (info.cause.httpCode == CONFLICT) {
				conflictErrorEvent.call()
			}
		}
	}

	protected inline fun <reified T> Flow<T>.asLiveData(
		context: CoroutineContext = Dispatchers.IO,
		scope: CoroutineScope = viewModelScope
	): LiveData<T> = object : MutableLiveData<T>() {
		private var job: Job? = null

		override fun onActive() {
			super.onActive()
			job?.cancel()
			job = flowOn(context)
				.onEach { postValue(it) }
				.launchIn(scope)
		}

		override fun onInactive() {
			super.onInactive()
			job?.cancel()
		}
	}

	@MainThread
	protected fun <T1> observeForever(
		p1: LiveData<T1>,
		block: (T1) -> Unit
	): OneTimeAction<T1> {
		val action = OneTimeAction<T1> { block(it) }
		p1.observeDisposing {
			action.invoke(it)
		}
		return action
	}

	@MainThread
	protected fun <T1, T2> observeForever(
		p1: LiveData<T1>,
		p2: LiveData<T2>,
		block: (T1, T2) -> Unit
	): OneTimeAction<Pair<T1, T2>> {
		val action = OneTimeAction<Pair<T1, T2>> {
			block(it.first, it.second)
		}
		p1.observeDisposing {
			if (p1.value != null && p2.value != null) {
				action.invoke(Pair(p1.value!!, p2.value!!))
			}
		}
		p2.observeDisposing {
			if (p1.value != null && p2.value != null) {
				action.invoke(Pair(p1.value!!, p2.value!!))
			}
		}
		return action
	}

	@MainThread
	protected fun <T1, T2, T3> observeForever(
		p1: LiveData<T1>,
		p2: LiveData<T2>,
		p3: LiveData<T3>,
		block: (T1, T2, T3) -> Unit
	): OneTimeAction<Triple<T1, T2, T3>> {
		val action = OneTimeAction<Triple<T1, T2, T3>> {
			block(it.first, it.second, it.third)
		}
		p1.observeDisposing {
			if (p1.value != null && p2.value != null && p3.value != null) {
				action.invoke(Triple(p1.value!!, p2.value!!, p3.value!!))
			}
		}
		p2.observeDisposing {
			if (p1.value != null && p2.value != null && p3.value != null) {
				action.invoke(Triple(p1.value!!, p2.value!!, p3.value!!))
			}
		}
		p3.observeDisposing {
			if (p1.value != null && p2.value != null && p3.value != null) {
				action.invoke(Triple(p1.value!!, p2.value!!, p3.value!!))
			}
		}
		return action
	}

	private fun <T> LiveData<T>.observeDisposing(observer: (T) -> Unit) {
		disposables += DisposableObserverImpl(this, observer).apply(::observeForever)
	}

	private class DisposableObserverImpl<T>(
		livaData: LiveData<T>,
		changed: (T) -> Unit
	) : ObserverImpl<T>(changed), Disposable {

		private val liveData = WeakReference(livaData)

		override fun dispose() {
			liveData.get()?.removeObserver(this)
		}
	}
}