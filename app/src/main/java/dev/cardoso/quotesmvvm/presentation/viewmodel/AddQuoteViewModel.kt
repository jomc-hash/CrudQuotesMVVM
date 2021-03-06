package dev.cardoso.quotesmvvm.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.cardoso.quotesmvvm.data.model.AddQuoteResponse
import dev.cardoso.quotesmvvm.data.model.QuoteModel
import dev.cardoso.quotesmvvm.data.model.QuoteRequest
import dev.cardoso.quotesmvvm.data.model.QuoteResponse
import dev.cardoso.quotesmvvm.domain.usecase.AddQuoteUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddQuoteViewModel @Inject constructor (val addQuoteUseCase: AddQuoteUseCase) : ViewModel() {
    private val addQuoteResponseMutableStateFlow= MutableStateFlow(QuoteResponse(false, "", listOf()))

    val addQuoteResponse: StateFlow<QuoteResponse> = addQuoteResponseMutableStateFlow

    fun addQuote(token:String, addQuoteRequest: QuoteRequest){

        viewModelScope.launch {
            addQuoteUseCase.addQuote(token, addQuoteRequest)?.collect{
                addQuoteResponseMutableStateFlow.value= it
            }
        }
    }
}


