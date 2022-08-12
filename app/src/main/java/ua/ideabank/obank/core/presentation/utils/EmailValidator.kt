package ua.ideabank.obank.core.presentation.utils

import android.util.Patterns
import ua.ideabank.obank.core.common.extension.nonNullString

class EmailValidator {

	fun validate(email: String?, regex: Regex = Patterns.EMAIL_ADDRESS.toRegex()): Boolean {
		return email.nonNullString.matches(regex)
	}

}