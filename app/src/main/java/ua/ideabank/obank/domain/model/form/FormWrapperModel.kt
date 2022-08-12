package ua.ideabank.obank.domain.model.form

data class FormWrapperModel(
    val code: Int? = null,
    val form: FormModel? = null,
    val formId: String? = null,
    val status: String? = null,
    val currentScreen: Int? = null,
    val totalScreens: Int? = null
)