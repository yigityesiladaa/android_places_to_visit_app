package com.placestovisit.ui.auth.signIn

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.placestovisit.R
import com.placestovisit.common.extensions.showToast
import com.placestovisit.common.utils.Utils
import com.placestovisit.databinding.FragmentSignInBinding
import com.placestovisit.ui.auth.AuthViewModel

class SignInFragment : Fragment() {

    private var _binding : FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private lateinit var _authViewModel: AuthViewModel
    private lateinit var _navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentSignInBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        registerEvents()
        listenEvents()
    }

    private fun init(view : View){
        _navController = Navigation.findNavController(view)
        _authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        _authViewModel.setContext(requireContext())
    }

    private fun registerEvents(){
        binding.btnSignIn.setOnClickListener(btnSignInClickListener)
        binding.btnSignUpText.setOnClickListener {
            _navController.navigate(R.id.action_signInFragment_to_signUpFragment)
        }
        binding.btnForgotPasswordText.setOnClickListener {
            _navController.navigate(R.id.action_signInFragment_to_forgotPasswordFragment)
        }
    }

    private val btnSignInClickListener = View.OnClickListener { view->
        val email = binding.etMail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if(email.isNotEmpty() && password.isNotEmpty()){
            _authViewModel.signIn(email,password)
        }else{
            showToast(getString(R.string.all_fields_required_text))
        }
    }

    private fun listenEvents() {
        _authViewModel.state.observe(viewLifecycleOwner) { baseState ->
            Utils.checkState(
                activity,
                baseState,
                null
            ) {
                _navController.navigate(R.id.action_signInFragment_to_homeFragment)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = _authViewModel.isUserLoggedIn()
        if(currentUser != null) _navController.navigate(R.id.action_signInFragment_to_homeFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}