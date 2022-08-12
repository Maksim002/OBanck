package ua.ideabank.obank.core.presentation.app

import android.content.Context

object CorePlatformApp {

    internal lateinit var appContext: Context
        private set

    @JvmStatic
    @JvmName("init")
    operator fun invoke(appContext: Context) {
        CorePlatformApp.appContext = appContext
    }
}