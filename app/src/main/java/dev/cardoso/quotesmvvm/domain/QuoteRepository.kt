package dev.cardoso.quotesmvvm.domain

import dev.cardoso.quotesmvvm.data.model.AddQuoteResponse
import dev.cardoso.quotesmvvm.data.model.QuoteModel
import dev.cardoso.quotesmvvm.data.model.QuoteRequest
import dev.cardoso.quotesmvvm.data.model.QuoteResponse
import kotlinx.coroutines.flow.Flow

interface QuoteRepository {
    suspend fun getQuotes(token: String): Flow<List<QuoteModel>>
        suspend fun getQuoteRandom(): Flow<QuoteModel>
        suspend fun getQuote(quoteId:Int): Flow<QuoteModel>
    suspend fun addQuote(token: String, quoteRequest: QuoteRequest): Flow<AddQuoteResponse>?

    suspend fun editQuote(token: String, quoteRequest: QuoteRequest, id: String): Flow<QuoteResponse>?

    suspend fun deleteQuote(  token: String,
                              id: Int): Flow<QuoteResponse>?

    suspend fun showQuote(token: String, id: Int): Flow<QuoteResponse>?

    suspend fun getQuotesResponse(token: String): Flow<QuoteResponse>?

}