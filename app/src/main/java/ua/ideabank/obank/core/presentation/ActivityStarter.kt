package ua.ideabank.obank.core.presentation

import android.content.Intent
import android.os.Bundle

interface ActivityStarter {

	fun startActivity(intent: Intent)

	fun startActivity(intent: Intent, bundle: Bundle?)

	fun startActivityForResult(intent: Intent, requestCode: Int)

	fun startActivityForResult(intent: Intent, requestCode: Int, bundle: Bundle?)
}