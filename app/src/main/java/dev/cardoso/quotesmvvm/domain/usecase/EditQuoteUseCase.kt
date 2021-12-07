package dev.cardoso.quotesmvvm.domain.usecase

import android.util.Log
import dev.cardoso.quotesmvvm.data.local.daos.QuoteDAO
import dev.cardoso.quotesmvvm.data.model.QuoteRequest
import dev.cardoso.quotesmvvm.data.model.QuoteResponse
import dev.cardoso.quotesmvvm.domain.QuoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


class EditQuoteUseCase  @Inject constructor (quoteDAO: QuoteDAO, var quoteRepositoryImpl: QuoteRepository){
    suspend fun editQuote(token:String, quoteRequest: QuoteRequest, id:String): Flow<QuoteResponse>?
    {
        return quoteRepositoryImpl.editQuote(token,quoteRequest, id)
    }
}