package ua.ideabank.obank.data.local.di


import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ua.ideabank.obank.data.local.AppLocalSource
import ua.ideabank.obank.data.local.UserLocalSource
import ua.ideabank.obank.data.local.helper.SharedPrefsHelper
import ua.ideabank.obank.data.local.impl.AppLocalSourceImpl
import ua.ideabank.obank.data.local.impl.UserLocalSourceImpl

val localDataModule = module {
    factory {
        SharedPrefsHelper(androidContext().getSharedPreferences("Obank", Context.MODE_PRIVATE))
    }
    factory<AppLocalSource> { AppLocalSourceImpl(get()) }
    factory<UserLocalSource> { UserLocalSourceImpl(get()) }
}