package ua.ideabank.obank.presentation.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ua.ideabank.obank.presentation.form.FormViewModel

val viewModelModule = module {
    viewModel { FormViewModel(get()) }
}