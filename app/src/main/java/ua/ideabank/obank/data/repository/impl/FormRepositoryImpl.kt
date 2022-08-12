package ua.ideabank.obank.data.repository.impl

import ua.ideabank.obank.data.local.AppLocalSource
import ua.ideabank.obank.data.local.UserLocalSource
import ua.ideabank.obank.data.network.service.FormApiService
import ua.ideabank.obank.data.repository.mapper.FormModelMapper
import ua.ideabank.obank.domain.model.form.FormWrapperModel
import ua.ideabank.obank.domain.repository.FormRepository

class FormRepositoryImpl(
    private var apiService: FormApiService,
    var userLocalSource: UserLocalSource,
    var appLocalSource: AppLocalSource,
    var mapper: FormModelMapper
) : FormRepository {

    override suspend fun getForm(screen: Int?): FormWrapperModel {
        //todo fix line
        val number =
            if (userLocalSource.phone.isNotEmpty()) userLocalSource.phone
            else "380688291904"
        val language = appLocalSource.language

        val response = apiService.getForm {
            "user_id" to number
            "command" to "form"
            "action" to "get"
            "localization" to language
            if (screen != null) {
                "screen" to screen
            }
        }.body!!.response!!
        return mapper.mapNetToModel(response)
    }
}