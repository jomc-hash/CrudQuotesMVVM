package dev.cardoso.quotesmvvm.presentation.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.cardoso.quotesmvvm.R
import dev.cardoso.quotesmvvm.data.model.QuoteModel
import dev.cardoso.quotesmvvm.databinding.ActivityQuoteListBinding
import dev.cardoso.quotesmvvm.domain.UserPreferencesRepository
import dev.cardoso.quotesmvvm.presentation.viewmodel.QuoteListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
        getToken()
        super.onCreate(savedInstanceState)
        binding = ActivityQuoteListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getToken()
        getQuotes("Bearer $token")

        setBtnAdd()
    }

    private fun getQuotes(token:String){
        lifecycleScope.launch{
            Log.w("josemdebug", "el token es ... $token")
            quoteListViewModel.getQuotes("Bearer $token")
            quoteListViewModel.quoteList.collect{
                binding.rvFrases.adapter= QuotesAdapter(it,
                    object:QuotesAdapter.OptionsClickListener{
                        override fun onUpdateQuote(quote: QuoteModel) {
                            adapterOnClick(quote)
                        }

                        override fun onDeleteQuote(quote: QuoteModel) {
                            deleteQuote("Bearer $token", id= quote.id)
                        }

                        override fun onMenuClicked(context: Context, position: Int, quote: QuoteModel) {
                            performOptionsMenuClick(context, position, quote)
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

    private fun performOptionsMenuClick(context: Context, position:Int, quote: QuoteModel) {
        // create object of PopupMenu and pass context and view where we want
        // to show the popup menu
        val popupMenu = PopupMenu(context, binding.rvFrases[position].findViewById(R.id.textViewOptions))
        // add the menu
        popupMenu.inflate(R.menu.options_menu)
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when (item?.itemId) {
                    R.id.deleteQuoteOption -> {
                        Toast.makeText(applicationContext, "Deleting quote", Toast.LENGTH_SHORT)
                            .show()
                        deleteQuote("Bearer $token", id= quote.id)
                    }
                    // in the same way you can implement others
                    R.id.editQuoteOption -> {
                        // define
                        Toast.makeText(applicationContext, "Edit quote", Toast.LENGTH_SHORT)
                            .show()
                        adapterOnClick(quote)
                        return true
                    }
                }
                return false
            }
        })
        popupMenu.show()
    }
    
    
}
