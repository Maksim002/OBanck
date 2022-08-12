package ua.ideabank.obank.core.presentation.permission

interface PermissionManager {

	fun PermissionAware.checkOrRequestPermission(
		requestCode: Int,
		vararg permissions: String,
		listener: OnRequestPermissionResultListener
	)

	fun PermissionAware.checkOrRequestPermission(
		requestCode: Int,
		vararg permissions: String,
		listener: (requestCode: Int, isGranted: Boolean, shouldShowRequestPermissionRationale: Boolean?) -> Unit
	)

	fun PermissionAware.onRequestPermissionResult(
		requestCode: Int,
		permissions: Array<out String>,
		grantResults: IntArray
	)

	fun PermissionAware.hasPermission(vararg permissions: String): Boolean

	interface OnRequestPermissionResultListener {
		fun onPermissionGranted(requestCode: Int)
		/**
		 * shouldShowRequestPermissionRationale - null is for pre 23 devices
		 */
		fun onPermissionDenied(requestCode: Int, shouldShowRequestPermissionRationale: Boolean?)
	}

}