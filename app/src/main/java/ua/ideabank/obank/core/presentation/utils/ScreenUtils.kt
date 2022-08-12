package ua.ideabank.obank.core.presentation.utils

import android.content.Context
import kotlin.math.pow
import kotlin.math.sqrt

object ScreenUtils {

    fun getScreenDiagonal(context: Context): Double {
        val metrics = context.resources.displayMetrics
        val height = (metrics.heightPixels / metrics.ydpi).toDouble()
        val with = (metrics.widthPixels / metrics.xdpi).toDouble()
        return sqrt(with.pow(2.0) + height.pow(2.0))
    }

}