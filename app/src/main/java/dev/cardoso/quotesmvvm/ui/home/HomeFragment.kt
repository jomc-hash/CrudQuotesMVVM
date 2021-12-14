package dev.cardoso.quotesmvvm.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
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
    private lateinit var quoteList: List<QuoteModel>
    private var token: String =""


    private val homeViewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private lateinit var binding2: FragmentHomeBinding

    // This property is only valid between onCreateView and
    // onDestroyView.
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

        Log.w("jdebug", "el token es $token")
        binding2 = FragmentHomeBinding.inflate(layoutInflater)


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.btnAgregarFrase.setOnClickListener {
            doSomething()
           // testViewModel("Bearer S")
        }
        getQuotes("Bearer $token")
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun doSomething (){
        if (quoteList.size>0){
            lastId= quoteList.elementAt(quoteList.lastIndex).id+1
        }
        val bundle = bundleOf("lastId" to lastId,)
        this.findNavController().navigate(R.id.action_nav_home_to_addQuoteFragment, bundle)

    }


    private fun getQuotes(token:String){
        viewLifecycleOwner.lifecycleScope.launch{
            Log.w("josemdebug", "el token es ... $token")

            homeViewModel.getQuotes("Bearer $token")
            homeViewModel.quoteList.collect{
                Log.w("jdebug", "$it")
                binding.rvFrases.adapter= QuotesAdapter(it,
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
    private fun testViewModel(token:String){
        this.lifecycleScope.launch{
            Log.w("josemdebug", "el token es ... $token")

           homeViewModel.getQuotes("Bearer $token")
        }
    }

    private fun adapterEditQuote (quote: QuoteModel) {
         //Toast.makeText(context, "El id es : ${quote.id}", Toast.LENGTH_LONG).show()
        val action = HomeFragmentDirections.actionNavHomeToEditQuoteFragment(
            quote.quote,
            quote.author
        )
        action.quoteId= quote.id
        this.findNavController().navigate(action)
    }

    private fun deleteQuote(token:String, id:Int){
        homeViewModel.deleteQuote(token, id)
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
                        Toast.makeText(context, "Deleting quote", Toast.LENGTH_SHORT)
                            .show()
                        deleteQuote("Bearer $token", id= quote.id)
                        getQuotes(token)
                        binding.rvFrases.adapter?.notifyDataSetChanged()
                    }
                    // in the same way you can implement others
                    R.id.editQuoteOption -> {
                        // define

                        Toast.makeText(context, "Edit quote", Toast.LENGTH_SHORT)
                            .show()
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
        getQuotes(token)
        binding.rvFrases.adapter?.notifyDataSetChanged()

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
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }else{
                    if (it.message!=""){
                        Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}