package dev.cardoso.quotesmvvm.presentation.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.cardoso.quotesmvvm.core.BASE_URL
import dev.cardoso.quotesmvvm.data.model.QuoteModel
import dev.cardoso.quotesmvvm.data.model.QuoteResponse
import dev.cardoso.quotesmvvm.data.remote.QuoteApi
import dev.cardoso.quotesmvvm.databinding.ActivityQuoteListBinding
import dev.cardoso.quotesmvvm.domain.UserPreferencesRepository
import dev.cardoso.quotesmvvm.domain.usecase.DeleteQuoteUseCase
import dev.cardoso.quotesmvvm.domain.usecase.GetQuotesUseCase
import dev.cardoso.quotesmvvm.presentation.viewmodel.QuoteListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

const val ARG_ID = "quoteId"
const val ARG_AUTHOR= "quoteAuthor"
const val ARG_QUOTE= "quoteText"
const val ARG_LAST_INDEX= "lastIndex"

@AndroidEntryPoint
class QuoteListActivity : AppCompatActivity() {
    private lateinit var userPreferencesRepository: UserPreferencesRepository
    var token=""

    private val quoteListViewModel: QuoteListViewModel by viewModels()
    private var lastId:Int=0
    private lateinit var quoteList: List<QuoteModel>
    private lateinit var binding: ActivityQuoteListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        userPreferencesRepository = UserPreferencesRepository(this@QuoteListActivity)
        super.onCreate(savedInstanceState)
        binding = ActivityQuoteListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getToken()
        getQuotes(token)

        setBtnAdd()
    }

    private fun getQuotes(token:String){
        lifecycleScope.launch{
            Log.w("josemdebug", "el token es ... $token")
            quoteListViewModel.getQuotes()
            quoteListViewModel.quoteList.collect{
                binding.rvFrases.adapter= QuotesAdapter(it,
                    object:QuotesAdapter.OptionsClickListener{
                        override fun onUpdateQuote(quote: QuoteModel) {
                            adapterOnClick(quote)
                        }

                        override fun onDeleteQuote(quote: QuoteModel) {
                            deleteQuote(token, id= quote.id)
                        }
                    }
                )
                quoteList=it

            }
        }

    }



    private fun setBtnAdd(){
        binding.btnAgregarFrase.setOnClickListener{
            lastId= quoteList.elementAt(quoteList.lastIndex).id
            Log.w("josemdebug", lastId.toString())
            val intent = Intent(this, AddQuoteActivity::class.java)
            intent.putExtra(ARG_LAST_INDEX, lastId)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        getQuotes(token)
    }


    private fun adapterOnClick (quote: QuoteModel) {
//         Toast.makeText(baseContext, "El id es : ${quote.id}", Toast.LENGTH_LONG).show()
        val intent = Intent(this, EditQuoteActivity()::class.java)
        intent.putExtra(ARG_ID, quote.id.toString())
        intent.putExtra(ARG_QUOTE, quote.quote)
        intent.putExtra(ARG_AUTHOR, quote.author)

        startActivity(intent)
    }

    private  fun deleteQuote(token:String, id:Int){
        quoteListViewModel.deleteQuote(token, id)
        this.onResume()
        binding.rvFrases.adapter?.notifyDataSetChanged()

    }

    private fun getToken(){
        lifecycleScope.launch (Dispatchers.IO){
            userPreferencesRepository.token.collect {
                token = it
           }
        }

    }

}
