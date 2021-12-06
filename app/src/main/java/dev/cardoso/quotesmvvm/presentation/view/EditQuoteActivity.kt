package dev.cardoso.quotesmvvm.presentation.view

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.cardoso.quotesmvvm.data.model.QuoteRequest
import dev.cardoso.quotesmvvm.databinding.ActivityEditQuoteBinding
import dev.cardoso.quotesmvvm.domain.UserPreferencesRepository
import dev.cardoso.quotesmvvm.presentation.viewmodel.EditQuoteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditQuoteActivity : AppCompatActivity() {
    private var quoteId:String = ""
    private var quoteAuthor:String = ""
    private var quoteText:String = ""

    private lateinit var binding: ActivityEditQuoteBinding
    private lateinit var userPreferencesRepository: UserPreferencesRepository

    private val editQuoteViewModel: EditQuoteViewModel by viewModels()

    private var token="token"
    override fun onCreate(savedInstanceState: Bundle?) {
        quoteId= intent.getStringExtra(ARG_ID)!!
        quoteAuthor= intent.getStringExtra(ARG_AUTHOR)!!
        quoteText= intent.getStringExtra(ARG_QUOTE)!!
        userPreferencesRepository = UserPreferencesRepository(this@EditQuoteActivity)
        getToken()
        super.onCreate(savedInstanceState)
        binding = ActivityEditQuoteBinding.inflate(layoutInflater)
        setValues()
        setContentView(binding.root)

        binding.btnEditQuote.setOnClickListener {

            val id= quoteId
            val quote = binding.etEditQuote.text.toString()
            val author = binding.etEditAuthor.text.toString()
            val quoteReq = QuoteRequest(id= id.toInt(), quote = quote, author = author)

            lifecycleScope.launch(Dispatchers.IO) {
                Log.e("jdebug", "token en editquote $token")
                editQuoteViewModel.editQuote("Bearer $token",
                    quoteReq,
                    id.toString())
            }
        }
        observer()
        setBtnCancelListener()
        setTextFieldsListener()
    }


    private fun getToken(){
        lifecycleScope.launch (Dispatchers.IO){
            userPreferencesRepository.token.collect {
                token = it
                Log.w("jdebug", "edit t getoken = \n$it")
                Log.w("jdebug", "edit t var = \n$it")
            }
        }

    }

    private  fun observer() {
        lifecycleScope.launch {
            editQuoteViewModel.quoteResponse.collect {
                binding.tvMessage.setText( it.message)
                Log.w("jdebug", "FRASES ENCONTRADAA = \n ${it.data.size}")
                it.data.forEach {
                    Log.w("jdebug", "FRASES = \n ${it.author} :::: ${it.quote}")
                }

            }
        }
    }

    private fun setValues(){
        binding.etEditAuthor.setText(quoteAuthor)
        binding.etEditQuote.setText(quoteText)
    }

    private fun setBtnCancelListener() {
        binding.btnCancelEdit.setOnClickListener{
            super.onBackPressed()
        }
    }

    fun setTextFieldsListener(){
        binding.etEditAuthor.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        })
        binding.etEditQuote.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        })
        binding.etEditQuote.setImeOptions(EditorInfo.IME_ACTION_DONE);
        binding.etEditAuthor.setRawInputType(InputType.TYPE_CLASS_TEXT);
        binding.etEditAuthor.setImeOptions(EditorInfo.IME_ACTION_DONE);
        binding.etEditQuote.setRawInputType(InputType.TYPE_CLASS_TEXT);
    }

    fun hideKeyboard(view: View) {
        val inputMethodManager: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }

}