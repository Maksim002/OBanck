package ua.ideabank.obank.core.common.exception

class UnauthorizedUserException(message: String, cause: Throwable? = null) :
	RuntimeException(message, cause)
