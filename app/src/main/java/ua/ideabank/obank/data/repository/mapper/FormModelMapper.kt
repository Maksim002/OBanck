package ua.ideabank.obank.data.repository.mapper

import ua.ideabank.obank.data.network.model.form.FieldNetModel
import ua.ideabank.obank.data.network.model.form.FormNetModel
import ua.ideabank.obank.data.network.model.form.FormWrapperNetModel
import ua.ideabank.obank.data.repository.mapper.base.BaseMapper
import ua.ideabank.obank.domain.model.form.FieldModel
import ua.ideabank.obank.domain.model.form.FormModel
import ua.ideabank.obank.domain.model.form.FormWrapperModel

class FormModelMapper : BaseMapper<FormWrapperModel, Any, FormWrapperNetModel>() {

    override fun mapNetToModel(netModel: FormWrapperNetModel): FormWrapperModel {
        return FormWrapperModel(
            code = netModel.code,
            form = netModel.form?.let { mapFormNetToModel(it) },
            formId = netModel.formId,
            status = netModel.status,
            currentScreen = netModel.currentScreen,
            totalScreens = netModel.totalScreens
        )
    }

    private fun mapFormNetToModel(netModel: FormNetModel): FormModel {
        return FormModel(
            title = netModel.title,
            description = netModel.description,
            fields = netModel.fields?.map { mapFieldNetToModel(it) }
        )
    }

    private fun mapFieldNetToModel(netModel: FieldNetModel): FieldModel {
        return FieldModel(
            clazz = netModel.clazz,
            error = netModel.error,
            id = netModel.id,
            items = netModel.items,
            placeholder = netModel.placeholder,
            required = netModel.required,
            soc = netModel.soc,
            title = netModel.title,
            type = netModel.type,
            value = netModel.value,
            visibility = netModel.visibility
        )
    }
}