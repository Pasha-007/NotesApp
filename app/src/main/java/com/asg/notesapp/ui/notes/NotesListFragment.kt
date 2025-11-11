package com.asg.notesapp.ui.notes

import androidx.appcompat.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.asg.notesapp.MainActivity
import com.asg.notesapp.R
import com.asg.notesapp.databinding.FragmentNotesListBinding
import com.asg.notesapp.util.UiState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class NotesListFragment : Fragment() {
    private var _binding : FragmentNotesListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotesViewModel by viewModel()

    private lateinit var notesAdapter: NotesAdapter



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotesListBinding.inflate(inflater, container, false)
        return binding.root
    }
    private fun setupRecyclerView() {
        notesAdapter = NotesAdapter { note ->
            val bundle = Bundle()
            bundle.putInt("noteId", note.id)
            findNavController().navigate(R.id.action_notesList_to_viewNote, bundle)
        }

        binding.recyclerViewNotes.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = notesAdapter
        }
    }

    private fun observeNotesState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.notesState.collect { state ->
                    when (state) {
                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.recyclerViewNotes.visibility = View.GONE
                            binding.emptyStateLayout.visibility = View.GONE
                        }
                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            val notes = state.data
                            if (notes.isEmpty()) {
                                binding.emptyStateLayout.visibility = View.VISIBLE
                                binding.recyclerViewNotes.visibility = View.GONE
                            } else {
                                binding.emptyStateLayout.visibility = View.GONE
                                binding.recyclerViewNotes.visibility = View.VISIBLE
                                notesAdapter.submitList(notes)
                            }
                        }
                        is UiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.recyclerViewNotes.visibility = View.GONE
                            binding.emptyStateLayout.visibility = View.VISIBLE
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                        }
                        UiState.Idle -> {
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                } // <-- closes collect
            }     // <-- closes repeatOnLifecycle
        }         // <-- closes launch
    }

    private fun setupClickListeners() {
        binding.fabAddNote.setOnClickListener {
            findNavController().navigate(R.id.action_notesList_to_createNote)
        }
        binding.iconInfo.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("About Notes App")
                .setMessage("This app lets you create, view, and manage notes. Version 1.0")
                .setPositiveButton("OK", null)
                .show()
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        setupRecyclerView()
        observeNotesState()
        setupClickListeners()
    }

    override fun onDestroyView() {
        binding.recyclerViewNotes.adapter = null
        _binding = null
        super.onDestroyView()
    }
}