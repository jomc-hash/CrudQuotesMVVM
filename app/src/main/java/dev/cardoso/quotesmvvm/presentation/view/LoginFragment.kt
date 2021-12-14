package dev.cardoso.quotesmvvm.presentation.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.cardoso.quotesmvvm.presentation.viewmodel.UserViewModel
import androidx.lifecycle.lifecycleScope
import dev.cardoso.quotesmvvm.data.model.LoginRequest
import dev.cardoso.quotesmvvm.databinding.FragmentLoginBinding
import dev.cardoso.quotesmvvm.domain.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch



@AndroidEntryPoint
class LoginFragment (): Fragment(){
    private var _binding: FragmentLoginBinding? = null
    private val binding get() =_binding!!

    private val userViewModel: UserViewModel by viewModels()
    private var token =""
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.btnLogin.setOnClickListener{
            val account = binding.etAccount.text.toString()
            val password =binding.etPassword.text.toString()
          //  val account = "josem"
          //  val password ="carrizosa"
            userViewModel.loginRequest(LoginRequest(account, password))
            getToken()
        }
    userPreferencesRepository = activity?.let { UserPreferencesRepository(it.applicationContext) }!!
        observer()
    return binding.root
    }

    private fun observer(){
        lifecycleScope.launch{
            userViewModel.loginResponse.collect{
                if(it.success){
                    val token = it.data
                    Toast.makeText(activity, "El usuario se ha autenticado correctamente", Toast.LENGTH_LONG).show()
                    saveToken(token)

                }else{
                    if (it.message!=""){
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun saveToken(token: String){
        lifecycleScope.launch(Dispatchers.IO){
            userPreferencesRepository.saveTokenToDataStore(token)
        }
    }

    private fun getToken(){
        lifecycleScope.launch (Dispatchers.IO){
            userPreferencesRepository.token.collect {
                token = it
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}