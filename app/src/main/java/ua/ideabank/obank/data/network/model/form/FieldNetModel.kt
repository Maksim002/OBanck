package ua.ideabank.obank.data.network.model.form

import com.google.gson.annotations.SerializedName

data class FieldNetModel(
    @SerializedName("class")
    val clazz: String? = null,
    @SerializedName("error")
    val error: String? = null,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("items")
    val items: List<String>? = null,
    @SerializedName("placeholder")
    val placeholder: String? = null,
    @SerializedName("required")
    val required: Boolean? = null,
    @SerializedName("soc")
    val soc: Boolean? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("value")
    val value: String? = null,
    @SerializedName("visibility")
    val visibility: String? = null
)