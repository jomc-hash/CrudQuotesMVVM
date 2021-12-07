package dev.cardoso.quotesmvvm.data.remote

import dev.cardoso.quotesmvvm.core.API_PATH
import dev.cardoso.quotesmvvm.core.QUOTES_URL
import dev.cardoso.quotesmvvm.data.model.AddQuoteResponse
import dev.cardoso.quotesmvvm.data.model.QuoteModel
import dev.cardoso.quotesmvvm.data.model.QuoteRequest
import dev.cardoso.quotesmvvm.data.model.QuoteResponse
import retrofit2.Response
import retrofit2.http.*

interface QuoteApi {
    @Headers("Content-Type: application/json; charset=utf-8","Accept: */*; charset=utf-8")
    @GET("$QUOTES_URL")
    suspend fun getQuotes(@Header("Authorization")token: String): Response<QuoteResponse>

    @Headers("Content-Type: application/json; charset=utf-8","Accept: */*; charset=utf-8")
    @PUT("$QUOTES_URL/{id}")
    suspend fun editQuote(@Header("Authorization")token: String,
                          @Path("id") id:Int,
                          @Body quoteRequest: QuoteRequest
    ): Response<QuoteResponse>

    @Headers("Content-Type: application/json; charset=utf-8","Accept: */*; charset=utf-8")
    @POST("$QUOTES_URL/")
    suspend fun  addQuote(
        @Header("Authorization") token:String,
        @Body quoteRequest: QuoteRequest
    ): Response<AddQuoteResponse>

    @Headers("Content-Type: application/json; charset=utf-8","Accept: */*; charset=utf-8")
    @DELETE("$QUOTES_URL/{id}")
    suspend fun  deleteQuote(
        @Header("Authorization") token:String,
        @Path("id") id:Int): Response<QuoteResponse>

    @Headers("Content-Type: application/json; charset=utf-8","Accept: */*; charset=utf-8")
    @GET("$QUOTES_URL/{id}")
    suspend fun showQuote(
        @Header("Authorization") token:String,
        @Path("id") id:Int
    ): Response<QuoteResponse>

    @Headers("Content-Type: application/json; charset=utf-8","Accept: */*; charset=utf-8")
    @GET("$QUOTES_URL")
    suspend fun getQuotesResponse(
        @Header("Authorization") token:String): Response<QuoteResponse>
}