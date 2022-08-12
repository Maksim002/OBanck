package ua.ideabank.obank.core.presentation.utils

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.*
import ua.ideabank.obank.core.common.extension.addFlag
import ua.ideabank.obank.core.common.extension.containsFlag
import ua.ideabank.obank.core.common.extension.removeFlag
import ua.ideabank.obank.core.common.extension.toggleFlag
import ua.ideabank.obank.core.presentation.app.CorePlatformApp

@TargetApi(Build.VERSION_CODES.M)
object StatusBarUtils {

	private const val isApi23OrAbove = Build.VERSION_CODES.M >= 23
	private val Activity.isStatusBarIconLight
		get() = when {
			isApi23OrAbove -> window.decorView.systemUiVisibility containsFlag View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
			else -> false
		}
	val getStatusBarHeight by lazy {
		with(CorePlatformApp.appContext.resources) {
			val resourceId = getIdentifier("status_bar_height", "dimen", "android")
			if (resourceId > 0) getDimensionPixelSize(resourceId)
			else kotlin.math.ceil(((24) * displayMetrics.density).toDouble()).toInt()
		}
	}

	private val getNavigationBarHeight by lazy {
		with(CorePlatformApp.appContext.resources) {
			val resourceId: Int = getIdentifier("navigation_bar_height", "dimen", "android")
			if (resourceId > 0) getDimensionPixelSize(resourceId)
			else 0
		}
	}

//    fun autoConfigure(activity: Activity) {
//        if (activity.isNightModeEnabled) setDarkMode(activity)
//        else setLightMode(activity)
//    }

	fun setDarkMode(activity: Activity) {
		setIconsMode(activity, false)
	}

	fun setLightMode(activity: Activity) {
		setIconsMode(activity, true)
	}

	fun setIconsMode(activity: Activity, light: Boolean) {
		if (isApi23OrAbove && light != activity.isStatusBarIconLight) {
			val decor = activity.window.decorView
			var systemUiVisibility = decor.systemUiVisibility
			if (!light) {
				systemUiVisibility = systemUiVisibility addFlag View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
				decor.systemUiVisibility = systemUiVisibility
			} else if (systemUiVisibility containsFlag View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) {
				systemUiVisibility = systemUiVisibility toggleFlag View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
				decor.systemUiVisibility = systemUiVisibility
			}
		}
		setMiUiStatusBarIconsMode(activity, light)
		setMeizuStatusBarIconsMode(activity, light)
	}

	@SuppressLint("PrivateApi")
	private fun setMiUiStatusBarIconsMode(activity: Activity, lightIcon: Boolean) {
		try {
			val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
			val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
			val darkModeFlag = field.getInt(layoutParams)
			val extraFlagField = activity.window.javaClass.getMethod(
				"setExtraFlags",
				Int::class.javaPrimitiveType,
				Int::class.javaPrimitiveType
			)
			extraFlagField.invoke(activity.window, if (!lightIcon) 0 else darkModeFlag, darkModeFlag)
		} catch (e: Exception) {
		}
	}

	private fun setMeizuStatusBarIconsMode(activity: Activity, lightIcon: Boolean) {
		try {
			val layoutParams = activity.window.attributes
			val darkFlag = WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
			val meizuFlags = WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
			darkFlag.isAccessible = true
			meizuFlags.isAccessible = true
			val bit = darkFlag.getInt(null)
			val value = meizuFlags.getInt(layoutParams).let {
				if (!lightIcon) it removeFlag bit
				else it addFlag bit
			}
			meizuFlags.setInt(layoutParams, value)
			activity.window.attributes = layoutParams
		} catch (e: Exception) {
		}
	}

	fun getNavigationBarSize(context: Context): Int {
		val appUsableSize: Point = getAppUsableScreenSize(context)
		val realScreenSize: Point = getRealScreenSize(context)

		return if (appUsableSize.y < realScreenSize.y) {
			return getNavigationBarHeight
		} else 0
	}

	private fun getAppUsableScreenSize(context: Context): Point {
		val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
		val display: Display = windowManager.defaultDisplay
		val size = Point()
		display.getSize(size)
		return size
	}

	private fun getRealScreenSize(context: Context): Point {
		val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
		val display: Display = windowManager.defaultDisplay
		val size = Point()
		display.getRealSize(size)
		return size
	}
}
