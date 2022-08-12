package ua.ideabank.obank.data.network.authenticator

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okhttp3.*
import ua.ideabank.obank.data.local.UserLocalSource
import ua.ideabank.obank.data.Constant.Header.AUTHORIZATION
import ua.ideabank.obank.data.Constant.Header.BEARER
import ua.ideabank.obank.data.Constant.Header.HEADER_PASSWORD
import ua.ideabank.obank.data.Constant.Header.HEADER_USERNAME
import ua.ideabank.obank.data.network.model.base.ResponseNetModel
import ua.ideabank.obank.data.network.model.token.TokenNetModel

internal open class TokenAuthenticator(
    private val tokenApiService: TokenApiService,
    private val userLocalSource: UserLocalSource
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            mutex.withLock {
                val tokenResponse: ResponseNetModel<TokenNetModel> = getUpdatedToken()

                if (tokenResponse.code == 200) {
                    userLocalSource.token = tokenResponse.body!!.token

                    Log.e("Token", userLocalSource.token)
                    response.request.newBuilder()
                        .header(AUTHORIZATION, "$BEARER ${tokenResponse.body.token}")
                        .build()
                } else null
            }
        }
    }

    private suspend fun getUpdatedToken() = withContext(Dispatchers.IO) {
        val credentials = Credentials.basic(HEADER_USERNAME, HEADER_PASSWORD)
        tokenApiService.refreshToken(credentials)
    }

    private val mutex = Mutex()
}