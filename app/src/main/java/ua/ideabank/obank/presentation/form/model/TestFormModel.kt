package ua.ideabank.obank.presentation.form.model

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class TestFormModel (
    @SerializedName("action")
    @Expose
    var action: String? = null,

    @SerializedName("ref")
    @Expose
    var ref: String? = null,

    @SerializedName("screen")
    @Expose
    var screen: Int? = null,

    @SerializedName("count")
    @Expose
    var count: Int? = null,

    @SerializedName("info")
    @Expose
    var info: TestInfoModel? = null,

    @SerializedName("data")
    @Expose
    var data: List<TestDatumModel>? = null
)