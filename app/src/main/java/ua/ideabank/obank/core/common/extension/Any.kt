package ua.ideabank.obank.core.common.extension

val Any?.nonNullString get() = this?.toString() ?: ""