package ua.ideabank.obank.core.common.language

import androidx.annotation.Size
import java.util.*

enum class Language(
    val languageCode: String,
    val headerKey: String
) {
    UA("uk", "uk"),
    RU("ru", "ru");

    companion object {
        @Suppress("MemberVisibilityCanBePrivate")
        fun obtainFromDefaultLocale(): Language {
            return when (Locale.getDefault().language) {
                UA.languageCode -> UA
                RU.languageCode -> RU
                else -> UA
            }
        }

        fun obtainFromLanguageCode(@Size(2) languageCode: String?): Language {
            return values().firstOrNull { it.languageCode == languageCode } ?: obtainFromDefaultLocale()
        }
    }

    val locale: Locale get() = Locale(languageCode)
}

