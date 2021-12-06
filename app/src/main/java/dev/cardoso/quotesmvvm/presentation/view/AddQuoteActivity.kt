package dev.cardoso.quotesmvvm.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import dev.cardoso.quotesmvvm.databinding.ActivityAddQuoteBinding
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import android.text.InputType
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import dagger.hilt.android.AndroidEntryPoint
import dev.cardoso.quotesmvvm.data.model.*
import dev.cardoso.quotesmvvm.presentation.viewmodel.AddQuoteViewModel

@AndroidEntryPoint
class AddQuoteActivity : AppCompatActivity() {
    private val token: String =""
    private lateinit var binding: ActivityAddQuoteBinding
    private val addQuoteViewModel: AddQuoteViewModel by viewModels()

    private var lastIndex:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        lastIndex= intent.getIntExtra(ARG_LAST_INDEX, 0)+1
        Log.w("josemdebug", lastIndex.toString())
        super.onCreate(savedInstanceState)
        binding = ActivityAddQuoteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setBtnCancelListener()
        setBtnCreateListener()
        setTextFieldsListener()


    }

    private fun setBtnCancelListener() {
        binding.btnCancelAdd.setOnClickListener{
        super.onBackPressed()
        }
    }

    fun setBtnCreateListener() {
            binding.btnCreateQuote.setOnClickListener {

                if (!validInput() ){
                    Toast.makeText(this, "Author and Quote fields must not be empty", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "Saving Quote ...", Toast.LENGTH_SHORT).show()
                Log.w("josemdebug", "${binding.etQuote.text.toString()}, ${binding.etAuthor.text.toString()}")
                    Log.w("josemdebug", "${lastIndex.toString()}")

                    addQuoteViewModel.addQuote(token, QuoteRequest(binding.etQuote.text.toString(), binding.etAuthor.text.toString(), lastIndex))
                lifecycleScope.launch(){
                    addQuoteViewModel.addQuoteResponse.collect{
                        if(it.success){
                            Toast.makeText(baseContext, "Se guardó correctamente", Toast.LENGTH_LONG).show()
                        }else{
                            if (it.message!=""){
                                Toast.makeText(baseContext, it.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
                super.onBackPressed()
            }
        }
    }

    private fun validInput():Boolean{
        return binding.etAuthor.text.toString().isNotEmpty() &&
                binding.etQuote.text.toString().isNotEmpty()
    }


    fun hideKeyboard(view: View) {
        val inputMethodManager: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }

    fun setTextFieldsListener(){
       binding.etAuthor.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        })
        binding.etQuote.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        })
        binding.etQuote.setImeOptions(EditorInfo.IME_ACTION_DONE);
        binding.etAuthor.setRawInputType(InputType.TYPE_CLASS_TEXT);
        binding.etAuthor.setImeOptions(EditorInfo.IME_ACTION_DONE);
        binding.etQuote.setRawInputType(InputType.TYPE_CLASS_TEXT);
    }
}



