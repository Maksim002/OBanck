package ua.ideabank.obank.data.network.model.form

import com.google.gson.annotations.SerializedName

data class FormWrapperNetModel(
    @SerializedName("code")
    val code: Int? = null,
    @SerializedName("form")
    val form: FormNetModel? = null,
    @SerializedName("form_id")
    val formId: String? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("current_screen")
    val currentScreen: Int? = null,
    @SerializedName("total_screens")
    val totalScreens: Int? = null,
    @SerializedName("errors")
    val errors: Int? = null
)