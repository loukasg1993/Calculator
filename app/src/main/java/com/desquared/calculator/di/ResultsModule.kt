package com.desquared.calculator.di

import com.desquared.calculator.api.Repository
import com.desquared.calculator.presentation.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val resultsModule = module{
    single { Repository(get()) }
    viewModel { MainViewModel(get()) }
}