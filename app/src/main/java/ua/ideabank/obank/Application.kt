package ua.ideabank.obank

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ua.ideabank.obank.core.presentation.app.CorePlatformApp
import ua.ideabank.obank.data.db.di.databaseModule
import ua.ideabank.obank.data.local.di.localDataModule
import ua.ideabank.obank.data.network.di.networkModules
import ua.ideabank.obank.data.repository.di.repositoryModules
import ua.ideabank.obank.presentation.di.viewModelModule

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        CorePlatformApp(this)

        startKoin {
            androidContext(this@Application)
            androidLogger()
            modules(listOf(
                viewModelModule,
                localDataModule,
                databaseModule,
                *repositoryModules,
                *networkModules
            ))
        }
    }
}