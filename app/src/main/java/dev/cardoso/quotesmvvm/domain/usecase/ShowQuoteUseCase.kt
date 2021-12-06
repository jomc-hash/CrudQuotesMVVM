package dev.cardoso.quotesmvvm.domain.usecase

import dev.cardoso.quotesmvvm.data.model.QuoteResponse
import dev.cardoso.quotesmvvm.domain.QuoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShowQuoteUseCase @Inject constructor (val quoteRepositoryImpl: QuoteRepository){
    suspend fun showQuote(token:String, id:Int): Flow<QuoteResponse>?
    {
        return quoteRepositoryImpl.showQuote(token,id)
    }
}