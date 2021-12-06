package dev.cardoso.quotesmvvm.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.cardoso.quotesmvvm.data.model.AddQuoteResponse
import dev.cardoso.quotesmvvm.data.model.QuoteModel
import dev.cardoso.quotesmvvm.data.model.QuoteRequest
import dev.cardoso.quotesmvvm.domain.usecase.AddQuoteUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddQuoteViewModel @Inject constructor (val addQuoteUseCase: AddQuoteUseCase) : ViewModel() {
    private val addQuoteResponseMutableStateFlow= MutableStateFlow(AddQuoteResponse(false, "", QuoteModel(0,"","")))

    val addQuoteResponse: StateFlow<AddQuoteResponse> = addQuoteResponseMutableStateFlow

    fun addQuote(token:String, addQuoteRequest: QuoteRequest){
        Log.w("josemdebug", addQuoteRequest.toString())

        viewModelScope.launch {
            //addQuoteUseCase.addQuote(AddQuoteRequest("MIFRASE", "MIAUTOR"))?.collect{
            addQuoteUseCase.addQuote(token, addQuoteRequest)?.collect{
                addQuoteResponseMutableStateFlow.value= it
            }
        }
    }
}


