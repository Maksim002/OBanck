package ua.ideabank.obank.core.presentation.permission

import android.annotation.TargetApi
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import java.util.*

class PermissionManagerDelegate : PermissionManager {

	private val permissionListeners: HashMap<Int, PermissionManager.OnRequestPermissionResultListener?> = hashMapOf()

	override fun PermissionAware.checkOrRequestPermission(
		requestCode: Int,
		vararg permissions: String,
		listener: PermissionManager.OnRequestPermissionResultListener
	) {
		if (hasPermission(*permissions)) listener.onPermissionGranted(requestCode)
		else {
			permissionListeners[requestCode] = listener
			requestPermissionsSafely(requestCode, *permissions)
		}
	}

	override fun PermissionAware.checkOrRequestPermission(
		requestCode: Int,
		vararg permissions: String,
		listener: (requestCode: Int, isGranted: Boolean, shouldShowRequestPermissionRationale: Boolean?) -> Unit
	) {
		checkOrRequestPermission(
			requestCode = requestCode,
			permissions = permissions,
			listener = OnRequestPermissionResultAdapter(listener)
		)
	}

	@RequiresApi(Build.VERSION_CODES.M)
	override fun PermissionAware.onRequestPermissionResult(
		requestCode: Int,
		permissions: Array<out String>,
		grantResults: IntArray
	) {
		permissionListeners[requestCode]?.apply {
			permissionListeners.remove(requestCode)
			if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
				this.onPermissionGranted(requestCode)
			} else {
				this.onPermissionDenied(
					requestCode,
					permissions
						.takeIf { Build.VERSION.SDK_INT >= 23 }
						?.any { shouldShowRequestPermissionRationale(it) }
				)
			}
		}
	}

	@TargetApi(Build.VERSION_CODES.M)
	override fun PermissionAware.hasPermission(vararg permissions: String): Boolean {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true

		val context = when (this) {
			is Activity -> this
			is Fragment -> activity
			else -> throw IllegalStateException("Only Activities and Fragments can request runtime permissions!")
		} ?: return false

		return permissions.none { ActivityCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED }
	}

	@TargetApi(Build.VERSION_CODES.M)
	private fun PermissionAware.requestPermissionsSafely(requestCode: Int, vararg permissions: String) {
		requestPermissions(*permissions, requestCode = requestCode)
	}

	private class OnRequestPermissionResultAdapter(
		private val listener: (requestCode: Int, isGranted: Boolean, shouldShowRequestPermissionRationale: Boolean?) -> Unit
	) : PermissionManager.OnRequestPermissionResultListener {

		override fun onPermissionGranted(requestCode: Int) {
			listener(requestCode, true, null)
		}

		override fun onPermissionDenied(requestCode: Int, shouldShowRequestPermissionRationale: Boolean?) {
			listener(requestCode, false, shouldShowRequestPermissionRationale)
		}
	}
}