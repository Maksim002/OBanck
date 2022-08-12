package ua.ideabank.obank.data.network.service

import retrofit2.http.Body
import retrofit2.http.POST
import ua.ideabank.obank.data.network.model.base.ResponseNetModel
import ua.ideabank.obank.data.network.model.form.FormNetResponse
import ua.ideabank.obank.data.network.utils.json.JsonObjectBody

@JvmSuppressWildcards
interface FormApiService {

    @POST("proxy/receiver")
    suspend fun getForm(
        @Body body: JsonObjectBody
    ): ResponseNetModel<FormNetResponse>

}