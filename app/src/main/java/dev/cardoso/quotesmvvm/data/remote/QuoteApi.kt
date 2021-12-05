package dev.cardoso.quotesmvvm.data.remote

import dev.cardoso.quotesmvvm.core.API_PATH
import dev.cardoso.quotesmvvm.data.model.QuoteModel
import dev.cardoso.quotesmvvm.data.model.QuoteResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT

interface QuoteApi {
    @GET("$API_PATH")
    suspend fun getQuotes(): Response<List<QuoteModel>>

    @PUT("$API_PATH")
    suspend fun editQuote(token: String, id: Int, quoteModel: QuoteModel): Response<QuoteResponse>

}