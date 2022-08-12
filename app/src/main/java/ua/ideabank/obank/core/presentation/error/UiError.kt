package ua.ideabank.obank.core.presentation.error

import ua.ideabank.obank.core.common.exception.ApiException

sealed class UiError(val message: String, open val cause: Throwable?) {
	override fun toString() = "Error with message=$message, cause=$cause"
}

class ApiError(message: String, override val cause: ApiException) : UiError(message, cause)

class ForceLogoutError(message: String, override val cause: ApiException? = null) : UiError(message, cause)

class ServerInternalError(message: String, override val cause: ApiException? = null) : UiError(message, cause)

class NetworkConnectionError : UiError("Network connection error", null)

class UndefinedError(message: String, cause: Throwable? = null) : UiError(message, cause)