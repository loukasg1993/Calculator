package com.desquared.calculator.presentation.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desquared.calculator.api.Repository
import com.desquared.calculator.model.ConversionResponse
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    private val _conversionResult = MutableLiveData<ConversionResponse>()
    private val accessKey = "994c230f3d40de390f38c43c309eeb36"
    val conversionResult: LiveData<ConversionResponse>
    get() = _conversionResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

   fun fetchConversionResult(currency: String){
       viewModelScope.launch {
           try {
               val response = repository.convertCurrency(accessKey)
               response.currency = currency
               _conversionResult.postValue(response)
           }
           catch (e: Exception) {
               _errorMessage.postValue(e.message)
           }
       }
    }
}