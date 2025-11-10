package com.asg.notesapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.asg.notesapp.MainActivity
import com.asg.notesapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).showActionBar("Home")
        binding.textViewWelcome.text = "Hi! Welcome to Notes App"
        binding.textViewMessage.text = "Auth is working! Notes features coming soon..."
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}