package ua.ideabank.obank.presentation.form.model

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class TestDatumModel (
    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("class")
    @Expose
    var class_: String? = null,

    @SerializedName("value")
    @Expose
    var value: String? = null,

    @SerializedName("title")
    @Expose
    var title: String? = null,

    @SerializedName("type")
    @Expose
    var type: String? = null,

    @SerializedName("required")
    @Expose
    var required: Boolean? = null,

    @SerializedName("submit_on_change")
    @Expose
    var submitOnChange: Boolean? = null,

    @SerializedName("visibility")
    @Expose
    var visibility: String? = null,

    @SerializedName("placeholder")
    @Expose
    var placeholder: String? = null,

    @SerializedName("error")
    @Expose
    var error: String? = null,

    @SerializedName("items")
    @Expose
    var items: Array<String>? = null
)