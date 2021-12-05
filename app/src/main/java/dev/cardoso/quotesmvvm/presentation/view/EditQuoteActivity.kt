package dev.cardoso.quotesmvvm.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.cardoso.quotesmvvm.data.model.QuoteModel
import dev.cardoso.quotesmvvm.databinding.ActivityEditQuoteBinding
import dev.cardoso.quotesmvvm.domain.UserPreferencesRepository
import dev.cardoso.quotesmvvm.presentation.viewmodel.EditQuoteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditQuoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditQuoteBinding
    private lateinit var userPreferencesRepository: UserPreferencesRepository

    private val editQuoteViewModel: EditQuoteViewModel by viewModels()

    private var token=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditQuoteBinding.inflate(layoutInflater)
        userPreferencesRepository = UserPreferencesRepository(this@EditQuoteActivity)
        setContentView(binding.root)
        getToken()
        binding.btnEditQuote.setOnClickListener {
            val id= 3 //TODO:Read from intent param
            val quote = binding.etEditQuote.text.toString()
            val author = binding.etEditAuthor.text.toString()
            val quoteModel = QuoteModel(id= id, quote = quote, author = author)

            lifecycleScope.launch(Dispatchers.IO) {
                Log.e("jdebug", "token en editquote $token")
                editQuoteViewModel.editQuote(quoteModel, "Bearer $token")
            }
        }
        editQuoteViewModel.quoteResponse.let {  }
        observer()
    }


    private fun getToken() {
        lifecycleScope.launch(Dispatchers.IO) {
            userPreferencesRepository.token.collect { token=it }
        }
    }

    private  fun observer() {
        lifecycleScope.launch(Dispatchers.IO) {
            editQuoteViewModel.quoteResponse.collect {
                binding.tvMessage.text= it.message
            }
        }
    }

}