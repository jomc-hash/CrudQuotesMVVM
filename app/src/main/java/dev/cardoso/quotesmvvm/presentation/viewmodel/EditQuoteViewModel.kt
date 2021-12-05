package dev.cardoso.quotesmvvm.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.cardoso.quotesmvvm.data.model.QuoteModel
import dev.cardoso.quotesmvvm.data.model.QuoteResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditQuoteViewModel @Inject constructor (): ViewModel(){
    private val quoteResponseMutableStateFlow = MutableStateFlow(
        QuoteResponse(false, message="", data = listOf())
    )

    val quoteResponse: StateFlow<QuoteResponse> = quoteResponseMutableStateFlow

    fun editQuote(quoteModel: QuoteModel, s: String) {
    }

}
