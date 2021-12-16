package dev.cardoso.quotesmvvm.ui.home

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dev.cardoso.quotesmvvm.R
import dev.cardoso.quotesmvvm.data.model.QuoteModel
import dev.cardoso.quotesmvvm.databinding.FragmentHomeBinding
import dev.cardoso.quotesmvvm.domain.UserPreferencesRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment()  {
    private lateinit var userPreferencesRepository: UserPreferencesRepository

    private var lastId= 0
    private var quoteList: List<QuoteModel> = listOf()
    private var token: String =""


    private val homeViewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userPreferencesRepository = UserPreferencesRepository(requireActivity())
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getToken()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.btnAgregarFrase.setOnClickListener {
            addQuote()
        }
        observer()
      //  getQuotes("Bearer $token")
        getQuotesAsResponse()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addQuote (){
        if (quoteList.size>0){
            lastId= quoteList.elementAt(quoteList.lastIndex).id+1
        }
        val bundle = bundleOf("lastId" to lastId,)
        this.findNavController().navigate(R.id.action_nav_home_to_addQuoteFragment, bundle)
    }


    private fun getQuotes(token:String){
        viewLifecycleOwner.lifecycleScope.launch{

            homeViewModel.getQuotes("Bearer $token")
            homeViewModel.quoteList.collect{
                Log.w("jdebug", "$it")
                //binding.rvFrases.adapter
                val adapter = QuotesAdapter(it,
                    object:QuotesAdapter.OptionsClickListener{
                        override fun onMenuClicked(context: Context, position: Int, quote: QuoteModel) {
                            performOptionsMenuClick(context, position, quote)
                        }
                    }
                )
                quoteList=it

            }
        }

    }

    private fun adapterEditQuote (quote: QuoteModel) {
        val action = HomeFragmentDirections.actionNavHomeToEditQuoteFragment(
            quote.quote,
            quote.author
        )
        action.quoteId= quote.id
        this.findNavController().navigate(action)
    }

    private  fun deleteQuote(token:String, id:Int){
        homeViewModel.deleteQuote(token, id)
        getQuotesAsResponse()

        binding.rvFrases.adapter?.notifyDataSetChanged()
        var action = DialogInterface.OnClickListener{ dialog, id ->}
        viewLifecycleOwner.lifecycleScope.launch {
                homeViewModel.delteQuoteResponse.collect{
                    if(it.success){
                        showAlertDialog(it.message,action)
                    }else {
                        if (it.message != "") {
                            if (it.message == "The jwt is expired." || it.message=="JWT is necessary") {
                                action = DialogInterface.OnClickListener { dialog, id ->
                                    val action =
                                        HomeFragmentDirections.actionNavHomeToNavLogin()
                                    this@HomeFragment.findNavController().navigate(action)
                                }
                            }
                            showAlertDialog(it.message,action)
                        }

                    }
            }

        }
    }


    private fun performOptionsMenuClick(context: Context, position:Int, quote: QuoteModel) {
        // create object of PopupMenu and pass context and view where we want
        // to show the popup menu
        val popupMenu = PopupMenu(context,
            _binding?.rvFrases?.get(position)?.findViewById(R.id.textViewOptions)
        )
        // add the menu
        popupMenu.inflate(R.menu.options_menu)
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when (item?.itemId) {
                    R.id.deleteQuoteOption -> {
                        //Toast.makeText(context, "Deleting quote", Toast.LENGTH_SHORT).show()
                        deleteQuote("Bearer $token", id= quote.id)
                        getQuotesAsResponse()
                    }
                    // in the same way you can implement others
                    R.id.editQuoteOption -> {
                        // define
                        adapterEditQuote(quote)
                        return true
                    }
                }
                return false
            }
        })
        popupMenu.show()
    }

    override fun onResume() {
        super.onResume()
        getQuotesAsResponse()
    //   getQuotes(token)
     //   binding.rvFrases.adapter?.notifyDataSetChanged()
    }

    private fun getToken(){
        lifecycleScope.launch (Dispatchers.IO){
            userPreferencesRepository.token.collect {
                token = it
                Log.w("jdebug", " fun getoken home::: $it")
            }
        }
    }

    private fun observer(){
        lifecycleScope.launch{
            homeViewModel.quoteResponse.collect{
                if(it.success){
                    quoteList= it.data
                    var mutableList:MutableList<QuoteModel> = mutableListOf<QuoteModel>()
                   quoteList.map {
                       mutableList.add(it)
                   }
                    binding.rvFrases.adapter= QuotesAdapter(quoteList,
                        object:QuotesAdapter.OptionsClickListener{
                            override fun onMenuClicked(context: Context, position: Int, quote: QuoteModel) {
                                performOptionsMenuClick(context, position, quote)
                            }
                        }
                    )
                }else{
                    if (it.message!=""){
                        Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun deleteQuoteObserver(){
            var action = DialogInterface.OnClickListener{ dialog, id ->
            }
            lifecycleScope.launch(){
                homeViewModel.delteQuoteResponse.collect{
                    if(it.success){
                        showAlertDialog(it.message,action)
                    }else {
                        if (it.message != "") {
                            if (it.message == "The jwt is expired." || it.message=="JWT is necessary") {
                                action = DialogInterface.OnClickListener { dialog, id ->
                                    val action =
                                        HomeFragmentDirections.actionNavHomeToNavLogin()
                                    this@HomeFragment.findNavController().navigate(action)
                                }
                            }
                            showAlertDialog(it.message,action)
                        }

                    }
                }
            }
    }
    private fun showAlertDialog(message:String, listener: DialogInterface.OnClickListener){
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Response")
        builder.setMessage("$message")
        builder.setPositiveButton("OK",  listener)
        builder.create()
        builder.show()
    }

    private fun getQuotesAsResponse(){
        viewLifecycleOwner.lifecycleScope.launch{
            homeViewModel.getQuotesAsResponse("Bearer $token")

        }
    }
}

