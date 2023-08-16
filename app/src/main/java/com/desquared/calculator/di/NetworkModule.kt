package com.desquared.calculator.di

import com.desquared.calculator.api.ApiClient
import org.koin.dsl.module

val networkModule = module {
    single { ApiClient.create() }
}