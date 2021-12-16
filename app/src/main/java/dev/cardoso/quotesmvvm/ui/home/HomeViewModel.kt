package dev.cardoso.quotesmvvm.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.cardoso.quotesmvvm.data.model.QuoteModel
import dev.cardoso.quotesmvvm.data.model.QuoteResponse
import dev.cardoso.quotesmvvm.domain.usecase.DeleteQuoteUseCase
import dev.cardoso.quotesmvvm.domain.usecase.GetQuotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel  @Inject constructor(private val getQuotesUseCase: GetQuotesUseCase, private val deleteQuoteUseCase: DeleteQuoteUseCase): ViewModel() {
    val token=""
    private var lastIndex:Int=0
    private val quoteResponseMutableStateFlow= MutableStateFlow(QuoteResponse(false, "", listOf()))
    val quoteResponse: StateFlow<QuoteResponse> = quoteResponseMutableStateFlow

    private var quoteListMutableStateFlow = MutableStateFlow<List<QuoteModel>>(listOf())
    val quoteList: StateFlow<List<QuoteModel>> = quoteListMutableStateFlow

    private val deleteQuoteResponseMutableStateFlow= MutableStateFlow(QuoteResponse(false, "", listOf()))
    val delteQuoteResponse: StateFlow<QuoteResponse> = deleteQuoteResponseMutableStateFlow

    fun getQuotes(token:String){
        Log.w("jdebug", "viewmodel")
    viewModelScope.launch {
            getQuotesUseCase.getQuotes(token)?.collect {
              quoteListMutableStateFlow.value= it
            }
        }
    }


    fun deleteQuote(token:String, id:Int){
        viewModelScope.launch {
            deleteQuoteUseCase.deleteQuote(token, id)?.collect {
                deleteQuoteResponseMutableStateFlow.value=it
            }
        }
    }

     fun getQuotesAsResponse(token:String){
        viewModelScope.launch {
            getQuotesUseCase.getRemoteQuotes(token)?.collect {
                quoteResponseMutableStateFlow.value= it
            }
        }
    }

}