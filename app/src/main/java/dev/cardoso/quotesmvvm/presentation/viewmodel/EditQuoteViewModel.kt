package dev.cardoso.quotesmvvm.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.cardoso.quotesmvvm.data.model.QuoteModel
import dev.cardoso.quotesmvvm.data.model.QuoteRequest
import dev.cardoso.quotesmvvm.data.model.QuoteResponse
import dev.cardoso.quotesmvvm.domain.usecase.EditQuoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditQuoteViewModel @Inject constructor (val editQuoteUseCase:EditQuoteUseCase): ViewModel(){
    private val quoteResponseMutableStateFlow = MutableStateFlow(
        QuoteResponse(false, message="", data = listOf())
    )

    val quoteResponse: StateFlow<QuoteResponse> = quoteResponseMutableStateFlow

    fun editQuote(token: String,
                  quoteRequest: QuoteRequest,
                  id: String) {
        viewModelScope.launch{
            val quoteResponse = editQuoteUseCase.editQuote(token, quoteRequest, id)?.first()
            quoteResponse.let{
                if (quoteResponse != null){
                    quoteResponseMutableStateFlow.value=quoteResponse
                }
            }
        }
    }

}
