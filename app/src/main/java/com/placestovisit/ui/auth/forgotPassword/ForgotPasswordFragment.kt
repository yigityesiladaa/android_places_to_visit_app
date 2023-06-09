package com.placestovisit.ui.auth.forgotPassword

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.placestovisit.common.utils.Utils
import com.placestovisit.databinding.FragmentForgotPasswordFragmentBinding
import com.placestovisit.ui.auth.AuthViewModel
class ForgotPasswordFragment : Fragment() {

    private var _binding : FragmentForgotPasswordFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var _authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        registerEvents()
        listenEvents()
    }

    private fun init(){
        _authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
    }

    private fun registerEvents(){
        binding.btnSubmit.setOnClickListener {
            val email = binding.etMail.text.toString().trim()
            _authViewModel.resetPassword(email)
        }
    }

    private fun listenEvents(){
        _authViewModel.state.observe(viewLifecycleOwner){ baseState->
            Utils.checkState(
                activity,
                baseState,
                null
            ){
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}