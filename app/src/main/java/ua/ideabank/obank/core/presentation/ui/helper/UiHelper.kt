package ua.ideabank.obank.core.presentation.ui.helper

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import ua.ideabank.obank.core.presentation.error.UiError
import ua.ideabank.obank.databinding.IncludeProgressBinding

class UiHelper(private val activity: Context) {
	private var errorToast: Toast? = null
	private var uploadingDialog: AlertDialog? = null

	fun showError(error: UiError?) {
		errorToast?.cancel()
		errorToast = Toast.makeText(activity, error?.message, Toast.LENGTH_LONG)
		errorToast?.show()
	}

	private fun showUploading() {
		if (uploadingDialog == null) {
			uploadingDialog = AlertDialog.Builder(activity)
				.setView(IncludeProgressBinding.inflate(LayoutInflater.from(activity)).root)
				.show()
			uploadingDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
			uploadingDialog!!.setCancelable(false)
		} else {
			uploadingDialog!!.show()
		}
	}

	private fun hideUploading() {
		if (uploadingDialog != null && uploadingDialog?.window?.decorView?.parent != null) {
			uploadingDialog!!.dismiss()
		}
	}

	fun showUploading(uploadingState: Boolean) {
		if (uploadingState) {
			showUploading()
		} else {
			hideUploading()
		}
	}
}