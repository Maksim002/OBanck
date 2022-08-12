package ua.ideabank.obank.data.network.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.ideabank.obank.data.network.utils.factory.converter.DslGsonConverterFactory
import ua.ideabank.obank.BuildConfig
import ua.ideabank.obank.core.common.Constant
import ua.ideabank.obank.data.network.authenticator.TokenAuthenticator
import ua.ideabank.obank.data.network.utils.deserializer.DateDeserializer
import ua.ideabank.obank.data.network.utils.factory.call.ResponseNetModelCallAdapterFactory
import ua.ideabank.obank.data.network.utils.serializer.DateSerializer
import java.util.*
import java.util.concurrent.TimeUnit

val apiModule = module {

    single { provideGson() }
    single { CoroutineCallAdapterFactory() }
    single { GsonConverterFactory.create() }
    single<CallAdapter.Factory> { ResponseNetModelCallAdapterFactory.create() }
    single<Converter.Factory> { DslGsonConverterFactory.invoke(gson = get()) }
    factory<Authenticator> { TokenAuthenticator(get(), get()) }
    factory { provideOkHttp() }
    factory(named(Constant.Di.TOKEN)) { provideOkHttp() }
    factory(named(Constant.Di.AWS)) { provideOkHttp() }

    //Default Retrofit Dependency
    single {
        provideRetrofit(
            hostName = BuildConfig.SERVER_URL,
            clientBuilder = get(),
            converterFactory = get(),
            adapterFactory = get(),
            authenticator = get()
        )
    }
    //Auth Token Retrofit Dependency
    single(named(Constant.Di.TOKEN)) {
        provideRetrofit(
            hostName = BuildConfig.SERVER_URL,
            clientBuilder = get(named(Constant.Di.TOKEN)),
            converterFactory = get(),
            adapterFactory = get(),
        )
    }
    //AWS Retrofit Dependency
    single(named(Constant.Di.AWS)) {
        provideRetrofit(
            hostName = BuildConfig.AWS_URL,
            clientBuilder = get(named(Constant.Di.AWS)),
            converterFactory = get(),
            adapterFactory = get(),
        )
    }
}

private fun provideRetrofit(
    hostName: String,
    clientBuilder: OkHttpClient.Builder,
    converterFactory: Converter.Factory,
    adapterFactory: CallAdapter.Factory,
    authenticator: Authenticator? = null
): Retrofit {
    authenticator?.let { clientBuilder.authenticator(it) }

    configureOkHttpInterceptors(clientBuilder)

    return Retrofit.Builder()
        .baseUrl(hostName)
        .client(clientBuilder.build())
        .addConverterFactory(converterFactory)
        .addCallAdapterFactory(adapterFactory)
        .build()
}

fun provideOkHttp(): OkHttpClient.Builder {
    return OkHttpClient.Builder()
        .connectTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
}

private fun provideGson(): Gson {
    val dateType = object : TypeToken<Date>() {}.type
    val dateTypeNullable = object : TypeToken<Date?>() {}.type

    return GsonBuilder()
        .registerTypeAdapter(dateType, DateDeserializer())
        .registerTypeAdapter(dateType, DateSerializer())
        .registerTypeAdapter(dateTypeNullable, DateDeserializer())
        .registerTypeAdapter(dateTypeNullable, DateSerializer())
        .setPrettyPrinting()
        .serializeNulls()
        .create()
}

private fun configureOkHttpInterceptors(
    okHttpClientBuilder: OkHttpClient.Builder
) {

    // OkHttp Logger
    val logger = HttpLoggingInterceptor()
    logger.level = HttpLoggingInterceptor.Level.BODY
    okHttpClientBuilder.addInterceptor(logger)

    // Stetho Logger
    okHttpClientBuilder.addNetworkInterceptor(StethoInterceptor())
}