package dev.cardoso.quotesmvvm.data.remote

import android.util.Log
import dev.cardoso.quotesmvvm.core.RetrofitHelper
import dev.cardoso.quotesmvvm.data.model.AddQuoteResponse
import dev.cardoso.quotesmvvm.data.model.QuoteModel
import dev.cardoso.quotesmvvm.data.model.QuoteRequest
import dev.cardoso.quotesmvvm.data.model.QuoteResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import javax.inject.Inject
//comment


val listaFrases = listOf<QuoteModel>(
    QuoteModel(1, "frase1", "autor1"),
    QuoteModel(2, "frase2", "autor1"),
    QuoteModel(3, "frase3", "autor1")
)

class QuoteApiImpl @Inject constructor(retrofit: Retrofit):QuoteApi{
    private val apiService: QuoteApi = retrofit.create(QuoteApi::class.java)

    override suspend fun getQuotes(token: String): Response<QuoteResponse> {
        return apiService.getQuotes(token)
    }

    override suspend fun editQuote(
        token: String,
        id: Int,
        quoteRequest: QuoteRequest
    ): Response<QuoteResponse> {
        Log.e("jdebug", "token en quoteapiimpl \n $token")
        Log.e("jdebug", "request \n ${quoteRequest.author}")

        return apiService.editQuote(token = token ,id= id.toInt(), quoteRequest = quoteRequest)
    }

    override suspend fun addQuote(
        token: String,
        quoteRequest: QuoteRequest
    ): Response<AddQuoteResponse> {
        Log.e("jdebug", "token en quoteapiimpl \n $token")
        Log.e("jdebug", "request \n ${quoteRequest.author}")

        return apiService.addQuote(token = token , quoteRequest = quoteRequest)
    }

    override suspend fun deleteQuote(
        token: String,
        id: Int
    ): Response<QuoteResponse> {
        return apiService.deleteQuote(token = token , id=id)
    }

    override suspend fun showQuote(token: String, id: Int): Response<QuoteResponse> {
        return apiService.showQuote(token = token , id=id)
    }

    override suspend fun getQuotesResponse(token: String): Response<QuoteResponse> {
        return apiService.getQuotes(token = token)
    }

}
