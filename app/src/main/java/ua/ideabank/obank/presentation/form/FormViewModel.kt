package ua.ideabank.obank.presentation.form

import androidx.lifecycle.MutableLiveData
import ua.ideabank.obank.core.presentation.ui.base.BaseViewModel
import ua.ideabank.obank.domain.model.form.FormWrapperModel
import ua.ideabank.obank.domain.repository.FormRepository

class FormViewModel(private val formRepository: FormRepository) : BaseViewModel() {
    val formResult = MutableLiveData<FormWrapperModel>()

    val currentPagePosition = MutableLiveData<Int>()
    val totalPageCount = MutableLiveData(0)
    val progress = MutableLiveData<Int>()

    init {
        getForm()

        observeForever(formResult) {
            it.totalScreens?.let { count ->
                totalPageCount.value = count
            }
        }

        observeForever(currentPagePosition) { page ->
            val pageNumber = page + 1
            progress.value = pageNumber

            if (pageNumber > 1) {
                getForm(pageNumber)
            }
        }
    }

    private fun getForm(page: Int? = null) {
        launch(formResult) {
            formRepository.getForm(page)
        }
    }
}