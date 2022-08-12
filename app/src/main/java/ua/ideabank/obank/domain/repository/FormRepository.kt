package ua.ideabank.obank.domain.repository

import ua.ideabank.obank.domain.model.form.FormWrapperModel

interface FormRepository {
    suspend fun getForm(screen: Int? = null) : FormWrapperModel
}