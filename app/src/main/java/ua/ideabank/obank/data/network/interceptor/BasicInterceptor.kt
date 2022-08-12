package ua.ideabank.obank.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import ua.ideabank.obank.data.local.UserLocalSource
import ua.ideabank.obank.data.Constant.Header.AUTHORIZATION
import ua.ideabank.obank.data.Constant.Header.BEARER
import java.io.IOException

internal class BasicInterceptor(
	private val userLocalSource: UserLocalSource
) : Interceptor {

	@Throws(IOException::class)
	override fun intercept(chain: Interceptor.Chain): Response {
		val original = chain.request()

		val token = original.header(AUTHORIZATION)
		val request = original.newBuilder()

		if (token != null) {
			val tokenPref = userLocalSource.token
			if (token != tokenPref) {
				request.header(AUTHORIZATION, "$BEARER ${userLocalSource.token}")
			}
		}

		request.method(original.method, original.body)
		return chain.proceed(request.build())
	}
}

