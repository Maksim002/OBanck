package ua.ideabank.obank.core.presentation.permission

interface PermissionAware {

	fun requestPermissions(vararg permissions: String, requestCode: Int)

	fun shouldShowRequestPermissionRationale(permission: String): Boolean

}