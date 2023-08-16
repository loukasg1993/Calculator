package com.desquared.calculator.api

import com.desquared.calculator.model.ConversionResponse

class Repository(private val apiService: ApiService) {

    suspend fun convertCurrency(accessKey: String): ConversionResponse {
        return apiService.convertCurrency(accessKey)
    }

}