package dev.cardoso.quotesmvvm.domain.usecase

import dev.cardoso.quotesmvvm.data.model.AddQuoteResponse
import dev.cardoso.quotesmvvm.data.model.QuoteRequest
import dev.cardoso.quotesmvvm.domain.QuoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddQuoteUseCase @Inject constructor ( val quoteRepositoryImpl: QuoteRepository){
    suspend fun addQuote(token:String, quoteRequest: QuoteRequest): Flow<AddQuoteResponse>?
    {
        return quoteRepositoryImpl.addQuote(token,quoteRequest)
    }
}