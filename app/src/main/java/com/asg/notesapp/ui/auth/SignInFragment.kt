package com.asg.notesapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.asg.notesapp.MainActivity
import com.asg.notesapp.R
import com.asg.notesapp.databinding.FragmentSignInBinding
import com.asg.notesapp.util.UiState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).hideActionBar()
        setupClickListeners()
        observeAuthState()
    }

    private fun setupClickListeners() {
        binding.buttonSignIn.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            viewModel.signIn(email, password)
        }

        binding.textViewSignUpLink.setOnClickListener {
            findNavController().navigate(R.id.action_signIn_to_signUp)
        }

        binding.textViewForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_signIn_to_forgotPassword)
        }
    }

    private fun observeAuthState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authState.collect { state ->
                when (state) {
                    is UiState.Idle -> {
                        binding.progressBar.visibility = View.GONE
                        binding.buttonSignIn.isEnabled = true
                    }
                    is UiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.buttonSignIn.isEnabled = false
                    }
                    is UiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.buttonSignIn.isEnabled = true
                        Toast.makeText(requireContext(), "Sign in successful!", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_signIn_to_home)
                        viewModel.resetAuthState()
                    }
                    is UiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.buttonSignIn.isEnabled = true
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}