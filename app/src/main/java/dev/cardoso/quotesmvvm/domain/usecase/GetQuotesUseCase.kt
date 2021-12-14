package dev.cardoso.quotesmvvm.domain.usecase

import android.util.Log
import dev.cardoso.quotesmvvm.data.QuoteRepositoryImpl
import dev.cardoso.quotesmvvm.data.local.daos.QuoteDAO
import dev.cardoso.quotesmvvm.data.model.QuoteModel
import dev.cardoso.quotesmvvm.data.model.QuoteResponse
import dev.cardoso.quotesmvvm.domain.QuoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuotesUseCase @Inject constructor (quoteDAO: QuoteDAO, var quoteRepositoryImpl: QuoteRepository) {


    suspend fun getQuotes(token: String): Flow<List<QuoteModel>> = quoteRepositoryImpl.getQuotes(token)

    suspend fun getRemoteQuotes(token:String):Flow<QuoteResponse>? {
        Log.w("jdebug", "UseCase")
        return quoteRepositoryImpl.getQuotesResponse(token)
    }
}