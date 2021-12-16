package dev.cardoso.quotesmvvm.presentation.view

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dev.cardoso.quotesmvvm.R
import dev.cardoso.quotesmvvm.data.model.QuoteRequest
import dev.cardoso.quotesmvvm.databinding.FragmentAddQuoteBinding
import dev.cardoso.quotesmvvm.domain.UserPreferencesRepository
import dev.cardoso.quotesmvvm.presentation.viewmodel.AddQuoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddQuoteFragment : Fragment() {

    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private var token: String =""

    private val addQuoteViewModel: AddQuoteViewModel by viewModels()

    private  var _binding: FragmentAddQuoteBinding?  = null
    private val binding get()= _binding!!
    var lastIndex=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         lastIndex = arguments?.getInt("lastId")!!
        userPreferencesRepository = UserPreferencesRepository(requireActivity())
        getToken()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddQuoteBinding.inflate(inflater, container, false)
        val root:View = binding.root
        setBtnCancelListener()

        setBtnCreateListener()
        setTextFieldsListener()
        observer()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // methods

    private fun getToken(){
        lifecycleScope.launch (Dispatchers.IO){
            userPreferencesRepository.token.collect {
                token = it
            }
        }
    }

    private fun setBtnCancelListener() {
        binding.btnCancelAdd.setOnClickListener{
            this.findNavController().popBackStack()
//            this.findNavController().navigate(R.id.action_addQuoteFragment_to_nav_home)
        }
    }

    fun setTextFieldsListener(){
        binding.etAuthor.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        })
        binding.etQuote.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        })
        binding.etQuote.setImeOptions(EditorInfo.IME_ACTION_DONE);
        binding.etAuthor.setRawInputType(InputType.TYPE_CLASS_TEXT);
        binding.etAuthor.setImeOptions(EditorInfo.IME_ACTION_DONE);
        binding.etQuote.setRawInputType(InputType.TYPE_CLASS_TEXT);
    }

    fun hideKeyboard(view: View) {
        val inputMethodManager: InputMethodManager =
           requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }


    fun setBtnCreateListener() {
        binding.btnCreateQuote.setOnClickListener {
            if (!validInput() ){
            }
            else{
                Toast.makeText(requireContext(), "Saving Quote ...", Toast.LENGTH_SHORT).show()
                val id:Int = binding.etId.text.toString().toInt()
                addQuoteViewModel.addQuote("Bearer $token", QuoteRequest(binding.etQuote.text.toString(), binding.etAuthor.text.toString(), id))
            }
        }
    }
    private fun validInput():Boolean{
        if (!binding.etId.text.toString().isNotEmpty()){
            Toast.makeText(requireContext(), "Id is required", Toast.LENGTH_SHORT).show()
            return false
        }
        return true

      /*  return binding.etAuthor.text.toString().isNotEmpty() &&
                binding.etQuote.text.toString().isNotEmpty() &&
                binding.etId.text.toString().isNotEmpty()*/
    }

    private fun observer(){
        var action = DialogInterface.OnClickListener{ dialog, id ->
        }
        lifecycleScope.launch(){
            addQuoteViewModel.addQuoteResponse.collect{
                if(it.success){
                    showAlertDialog(it.message,DialogInterface.OnClickListener{ dialog, id ->
                        this@AddQuoteFragment.findNavController().popBackStack()
                    })
                }else {
                    if (it.message != "") {
                        if (it.message == "The jwt is expired." || it.message=="JWT is necessary") {
                            action = DialogInterface.OnClickListener { dialog, id ->
                                val action =
                                    AddQuoteFragmentDirections.actionAddQuoteFragmentToNavLogin()
                                this@AddQuoteFragment.findNavController().navigate(action)
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

}