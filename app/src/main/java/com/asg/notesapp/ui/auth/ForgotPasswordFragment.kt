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
import com.asg.notesapp.databinding.FragmentForgotPasswordBinding
import com.asg.notesapp.util.UiState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForgotPasswordFragment : Fragment() {
    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity)
        setupClickListeners()
        observeAuthState()
    }

    private fun setupClickListeners() {
        binding.buttonResetPassword.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            viewModel.resetPassword(email)
        }

        binding.textViewBackToSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_forgotPassword_to_signIn)
        }
    }

    private fun observeAuthState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.passwordResetState.collect { state ->
                when (state) {
                    is UiState.Idle -> {
                        binding.progressBar.visibility = View.GONE
                        binding.buttonResetPassword.isEnabled = true
                    }
                    is UiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.buttonResetPassword.isEnabled = false
                    }
                    is UiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.buttonResetPassword.isEnabled = true
                        Toast.makeText(requireContext(), state.data, Toast.LENGTH_SHORT).show() // Use state.data
                        findNavController().navigate(R.id.action_forgotPassword_to_signIn)
                        viewModel.resetPasswordState() // Reset the password state
                    }
                    is UiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.buttonResetPassword.isEnabled = true
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