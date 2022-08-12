package ua.ideabank.obank.core.common.exception

open class ApiException(
	/**
	 * Called api method.
	 */
	val method: String,
	/**
	 * Unique Api error code.
	 */
	val code: Int,
	/**
	 * Api error message.
	 */
	val details: String,
	/**
	 * HTTP status code.
	 */
	val httpCode: Int,
	/**
	 * Cause of Exception.
	 */
	override val cause: Throwable? = null
) : RuntimeException(
	"Api error: method=[$method], code=[$code], details=[$details]",
	cause
) {

	override fun toString(): String {
		return "ApiException(method=$method, code=$code, details=$details, httpCode=$httpCode, cause=$cause)"
	}

	companion object ErrorCode {
		const val UNKNOWN = -1

		// local error codes
		const val JSON_PARSE = 1
		const val SERVER_NOT_RESPONDING = 2
		const val SOCKET_TIMEOUT = 3

		// general
		const val BAD_REQUEST = 400
		const val TOKEN_EXPIRED = 401
		const val CONFLICT = 409
		@JvmStatic
		val SERVER_ERRORS: Array<Int> = arrayOf(
			500, 501, 502, 503, 504, 505, 507, 509, 510, 511,
			520, 521, 522, 523, 524, 598, 599
		)
		@JvmStatic
		val LOGOUT_CODES: Array<Int> = arrayOf(TOKEN_EXPIRED)
	}

}