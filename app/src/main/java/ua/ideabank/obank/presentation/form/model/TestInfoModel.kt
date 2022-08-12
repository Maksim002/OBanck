package ua.ideabank.obank.presentation.form.model

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class TestInfoModel (
    @SerializedName("title")
    @Expose
    var title: String? = null,

    @SerializedName("description")
    @Expose
    var description: String? = null
)