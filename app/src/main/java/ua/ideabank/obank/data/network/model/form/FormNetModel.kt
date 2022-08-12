package ua.ideabank.obank.data.network.model.form

import com.google.gson.annotations.SerializedName

data class FormNetModel(
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("fields")
    val fields: List<FieldNetModel>? = null
)