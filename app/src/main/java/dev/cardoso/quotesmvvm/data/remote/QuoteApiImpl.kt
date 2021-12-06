package dev.cardoso.quotesmvvm.data.remote

import dev.cardoso.quotesmvvm.core.RetrofitHelper
import dev.cardoso.quotesmvvm.data.model.QuoteModel
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

    override suspend fun getQuotes(): Response<List<QuoteModel>> {
        return apiService.getQuotes()
    }

    override suspend fun editQuote(
        token: String,
        id: Int,
        quoteModel: QuoteModel
    ): Response<QuoteResponse> {
       return apiService.editQuote(token = token , id= id, quoteModel = quoteModel)
    }
}
