package dev.cardoso.quotesmvvm.data

import android.util.Log
import dev.cardoso.quotesmvvm.data.model.AddQuoteResponse
import dev.cardoso.quotesmvvm.data.model.QuoteModel
import dev.cardoso.quotesmvvm.data.model.QuoteRequest
import dev.cardoso.quotesmvvm.data.model.QuoteResponse
import dev.cardoso.quotesmvvm.data.remote.QuoteApi
import dev.cardoso.quotesmvvm.data.remote.QuoteRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Response
import javax.inject.Inject


class QuoteRemoteDataSourceImpl @Inject constructor(var quotesApi:QuoteApi): QuoteRemoteDataSource  {
    override suspend fun getQuotes(token: String): Flow<List<QuoteModel>?> {
        val response =  quotesApi.getQuotes(token)
        return (response.body().let {
            flow { emit(it?.data) }
        })
    }

    override suspend fun editQuote(
        token: String,
        id: String,
        quoteRequest: QuoteRequest,

    ): Flow<QuoteResponse>? {
        return quotesApi.editQuote(token,id.toInt(), quoteRequest)?.let {
            responseToQuoteResponse(
                it
            )
        }
    }
    override suspend fun deleteQuote(
        token: String,
        id: Int
        ): Flow<QuoteResponse>? {
        return quotesApi.deleteQuote(token = token , id=id)?.let {
            responseToQuoteResponse(
                it
            )
        }
    }

    override suspend fun showQuote(token: String, id: Int): Flow<QuoteResponse>? {
        return quotesApi.showQuote(token = token , id=id)?.let {
            responseToQuoteResponse(
                it
            )
        }
    }

    override suspend fun getQuotesResponse(token: String): Flow<QuoteResponse>? {
        return quotesApi.getQuotes(token = token )?.let {
            responseToQuoteResponse(
                it
            )
        }
    }

    private fun responseToQuoteResponse(response: Response<QuoteResponse>): Flow<QuoteResponse>? {
        return (when (response.isSuccessful) {
            true -> {
                Log.e("jdebug", "respuesta \n ${response.body()?.message}")

                response.body().let {
                    flow {
                        if (it != null) {
                            emit(it)
                        }
                    }
                }
            }
            else -> {
                val jsonObject =
                    JSONTokener(response.errorBody()?.string()).nextValue() as JSONObject
                val quoteResponse = QuoteResponse(
                    success = false,
                    message = jsonObject.getString("message"), data = listOf()
                )
                Log.e("jdebug", "respuesta \n ${quoteResponse.message}")
                flow { emit(quoteResponse) }
            }
        })

    }

    override suspend fun addQuote(token:String, quoteRequest: QuoteRequest): Flow<AddQuoteResponse>? {
        val response =  quotesApi.addQuote (token, quoteRequest)
        Log.w("josemdebug", response.message())

        return (when(response.isSuccessful){
            true-> {
                response.body().let{
                    flow{
                        if(it!= null){
                            emit(it)
                        }
                    }

                }
            }
            else ->{
                val jsonObject = JSONTokener(response.errorBody()?.string()).nextValue() as JSONObject
                val addQuoteResponse = AddQuoteResponse(
                    success = false,
                    message = jsonObject.getString("message"), data = QuoteModel(0,"","")
                )
                Log.e("ERROR", addQuoteResponse.toString())
                flow { emit(addQuoteResponse) }
            }
        }
                )
    }
}