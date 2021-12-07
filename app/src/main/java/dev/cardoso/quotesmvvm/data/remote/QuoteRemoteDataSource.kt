package dev.cardoso.quotesmvvm.data.remote

import dev.cardoso.quotesmvvm.data.model.AddQuoteResponse
import dev.cardoso.quotesmvvm.data.model.QuoteModel
import dev.cardoso.quotesmvvm.data.model.QuoteRequest
import dev.cardoso.quotesmvvm.data.model.QuoteResponse
import kotlinx.coroutines.flow.Flow

interface QuoteRemoteDataSource {
    suspend  fun getQuotes(token: String): Flow<List<QuoteModel>?>
    suspend fun editQuote(token: String, id: String,
                          quoteRequest: QuoteRequest,
                         ): Flow<QuoteResponse>?
    suspend fun addQuote(token: String,
                          quoteRequest: QuoteRequest,
    ): Flow<AddQuoteResponse>?

    suspend fun deleteQuote(token: String,
                            id: Int
    ): Flow<QuoteResponse>?

    suspend fun showQuote(token: String, id: Int): Flow<QuoteResponse>?

    suspend fun getQuotesResponse(token: String): Flow<QuoteResponse>?

}