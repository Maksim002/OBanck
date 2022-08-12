package ua.ideabank.obank.data.network.utils.factory.call

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import ua.ideabank.obank.data.network.utils.calladapter.ResponseNetModelCallAdapter
import ua.ideabank.obank.data.network.model.base.ResponseNetModel
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

internal class ResponseNetModelCallAdapterFactory private constructor() : CallAdapter.Factory() {

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        return when (getRawType(returnType)) {
            Call::class.java -> {
                val callType = getParameterUpperBound(0, returnType as ParameterizedType)
                when (getRawType(callType)) {
                    ResponseNetModel::class.java -> {
                        val resultType = getParameterUpperBound(0, callType as ParameterizedType)
                        ResponseNetModelCallAdapter<Any>(resultType)
                    }
                    else -> null
                }
            }
            else -> null
        }
    }

    companion object {
        fun create(): ResponseNetModelCallAdapterFactory {
            return ResponseNetModelCallAdapterFactory()
        }
    }
}