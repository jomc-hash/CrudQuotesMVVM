package dev.cardoso.quotesmvvm.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.cardoso.quotesmvvm.databinding.ActivityMainBinding
import dev.cardoso.quotesmvvm.domain.UserPreferencesRepository
import dev.cardoso.quotesmvvm.presentation.viewmodel.QuoteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val quoteViewModel: QuoteViewModel by viewModels()
    private var token=""
    private lateinit var userPreferencesRepository: UserPreferencesRepository
    override  fun onCreate(savedInstanceState: Bundle?) {
        userPreferencesRepository = UserPreferencesRepository(this@MainActivity)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        print(this.applicationContext)
        quoteViewModel.setContext(this)
        quoteViewModel.getQuotes(token)
        observer()
        binding.viewContainer.setOnClickListener {
            quoteViewModel.randomQuote()
        }
    }


    private fun observer(){
            lifecycleScope.launch {
            quoteViewModel.quoteModel.collect {
                binding.tvQuote.text = it.quote
                binding.tvAuthor.text= it.author
            }
        }
    }

    private fun getToken(){
        lifecycleScope.launch (Dispatchers.IO){
            userPreferencesRepository.token.collect {
                token = it
            }
        }

    }
}