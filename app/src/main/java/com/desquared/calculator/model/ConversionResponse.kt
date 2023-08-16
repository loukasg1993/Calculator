package com.desquared.calculator.model

data class ConversionResponse(
    val success: Boolean,
    val date: String,
    val rates: Map<String, Double>,
    var currency: String
)


