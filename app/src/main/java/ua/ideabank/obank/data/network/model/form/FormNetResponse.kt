package ua.ideabank.obank.data.network.model.form

import com.google.gson.annotations.SerializedName

data class FormNetResponse(
    @SerializedName("response")
    val response: FormWrapperNetModel? = null,
    @SerializedName("result")
    val result: String? = null
)