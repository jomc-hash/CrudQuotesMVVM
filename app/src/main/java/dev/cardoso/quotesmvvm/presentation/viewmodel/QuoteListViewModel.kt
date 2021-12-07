package dev.cardoso.quotesmvvm.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.cardoso.quotesmvvm.core.BASE_URL
import dev.cardoso.quotesmvvm.data.model.QuoteModel
import dev.cardoso.quotesmvvm.data.model.QuoteResponse
import dev.cardoso.quotesmvvm.data.remote.QuoteApi
import dev.cardoso.quotesmvvm.domain.usecase.DeleteQuoteUseCase
import dev.cardoso.quotesmvvm.domain.usecase.GetQuotesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@HiltViewModel
class QuoteListViewModel @Inject constructor(private val getQuotesUseCase: GetQuotesUseCase, private val deleteQuoteUseCase: DeleteQuoteUseCase)
    : ViewModel()
{
    private val quoteResponseMutableStateFlow= MutableStateFlow(QuoteResponse(false, "", listOf()))
    private var lastIndex:Int=0
    val quoteResponse: StateFlow<QuoteResponse> = quoteResponseMutableStateFlow
    val token=""

    private var quoteListMutableStateFlow = MutableStateFlow<List<QuoteModel>>(listOf())
    val quoteList: StateFlow<List<QuoteModel>> = quoteListMutableStateFlow

    fun getQuotes(token:String){
        viewModelScope.launch {
            getQuotesUseCase.getQuotes(token)?.collect{
                Log.i("Quotes :::", it.toString())
                quoteListMutableStateFlow.value= it
            }
        }
    }

    fun deleteQuote(token:String, id:Int){
        viewModelScope.launch {
            deleteQuoteUseCase.deleteQuote(token, id)?.collect{
                Log.i("Quotes :::", it.toString())
                quoteResponseMutableStateFlow.value= it
            }
        }
    }

}
