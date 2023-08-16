package com.desquared.calculator.api


import com.desquared.calculator.model.ConversionResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("latest")
    suspend fun convertCurrency(
        @Query("access_key") accessKey: String,
    ): ConversionResponse

}