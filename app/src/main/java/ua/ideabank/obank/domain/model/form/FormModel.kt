package ua.ideabank.obank.domain.model.form

data class FormModel(
    val title: String? = null,
    val description: String? = null,
    val fields: List<FieldModel>? = null
)