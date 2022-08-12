package ua.ideabank.obank.domain.model.form

data class FieldModel(
    val clazz: String? = null,
    val error: String? = null,
    val id: String? = null,
    val items: List<String>? = null,
    val placeholder: String? = null,
    val required: Boolean? = null,
    val soc: Boolean? = null,
    val title: String? = null,
    val type: String? = null,
    val value: String? = null,
    val visibility: String? = null
)