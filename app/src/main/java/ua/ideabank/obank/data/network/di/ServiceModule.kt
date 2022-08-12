package ua.ideabank.obank.data.network.di

import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import ua.ideabank.obank.core.common.Constant
import ua.ideabank.obank.data.network.service.*

val apiServiceModule = module {

}

private inline fun <reified T : Any> Module.singleApiService(name: String? = null) {
    single<T> {
        if (name == null) {
            get<Retrofit>().create(T::class.java)
        } else {
            get<Retrofit>(named(name)).create(T::class.java)
        }
    }
}