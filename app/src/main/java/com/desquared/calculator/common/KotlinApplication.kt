package com.desquared.calculator.common

import android.app.Application
import com.desquared.calculator.di.networkModule
import com.desquared.calculator.di.resultsModule

import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin


class KotlinApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KotlinApplication)
            modules(listOf( networkModule, resultsModule ))
        }
    }
}
