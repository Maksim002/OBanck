package ua.ideabank.obank.data.network.utils.calladapter

import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.Request
import okio.Timeout
import retrofit2.*
import ua.ideabank.obank.data.network.extension.deserializeType
import ua.ideabank.obank.data.network.extension.getMemberStringOrEmpty
import ua.ideabank.obank.data.network.model.base.ResponseNetModel
import java.lang.reflect.Type

internal class ResponseNetModelCallAdapter<R> internal constructor(private val responseType: Type) :
    CallAdapter<R, Call<ResponseNetModel<R>>> {

    override fun responseType() = responseType

    override fun adapt(call: Call<R>): Call<ResponseNetModel<R>> = ResponseNetModelCall(call)

    abstract class CallDelegate<TIn, TOut>(
        protected val proxy: Call<TIn>
    ) : Call<TOut> {
        override fun execute(): Response<TOut> = throw NotImplementedError()
        final override fun enqueue(callback: Callback<TOut>) = enqueueImpl(callback)
        final override fun clone(): Call<TOut> = cloneImpl()

        override fun cancel() = proxy.cancel()
        override fun request(): Request = proxy.request()
        override fun isExecuted() = proxy.isExecuted
        override fun isCanceled() = proxy.isCanceled

        abstract fun enqueueImpl(callback: Callback<TOut>)
        abstract fun cloneImpl(): Call<TOut>
    }

    class ResponseNetModelCall<T>(proxy: Call<T>) : CallDelegate<T, ResponseNetModel<T>>(proxy) {

        override fun timeout(): Timeout {
            TODO("Not yet implemented")
        }

        override fun enqueueImpl(callback: Callback<ResponseNetModel<T>>) = proxy.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val code = response.code()
                if (code in 200 until 300) {

                    //try obtain custom message from response (error body)
                    val errorMessage = response.errorBody()?.let {
                        try {
                            Gson()
                                .fromJson<JsonObject>(it.string(), deserializeType<JsonObject>())
                                .getMemberStringOrEmpty("message")
                        } catch (e: Exception) {
                            null
                        }
                    }.takeIf { !it.isNullOrEmpty() } ?: response.message()

                    callback.onResponse(
                        this@ResponseNetModelCall,
                        Response.success(
                            ResponseNetModel(
                                response.code(),
                                errorMessage.takeIf { it.isNotEmpty() },
                                response.body()
                            )
                        )
                    )
                } else callback.onFailure(this@ResponseNetModelCall, HttpException(response))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.onFailure(this@ResponseNetModelCall, t)
            }
        })

        override fun cloneImpl() = ResponseNetModelCall(proxy.clone())
    }
}