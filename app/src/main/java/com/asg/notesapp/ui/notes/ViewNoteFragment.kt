package com.asg.notesapp.ui.notes

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
import com.asg.notesapp.R
import com.asg.notesapp.databinding.FragmentViewNoteBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class ViewNoteFragment : Fragment(){
    private var _binding : FragmentViewNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel : NotesViewModel by viewModel()
    private var noteId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Back arrow
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // Menu actions
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_edit -> {
                    val bundle = Bundle().apply { putInt("noteId", noteId) }
                    findNavController().navigate(R.id.action_viewNote_to_editNote, bundle)
                    true
                }
                // R.id.action_delete -> { /* show confirm → delete → navigateUp() */ true }
                else -> false
            }
        }



        //Read id from argument
        noteId = arguments?.getInt("noteId", -1) ?: -1
        if(noteId == -1){
            Toast.makeText(requireContext(), "Invalid Note", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
            return
        }

//        // 2) click listeners
//        binding.iconBack.setOnClickListener { findNavController().navigateUp() }
//        binding.iconEdit.setOnClickListener {
//            val bundle = Bundle().apply { putInt("noteId", noteId) }
//            findNavController().navigate(R.id.action_viewNote_to_editNote, bundle)
//        }
        // 3) load & observe
        viewModel.loadNoteById(noteId)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentNote.collect { note ->
                    if (note != null) {
                        binding.textViewTitle.text = note.title
                        binding.textViewContent.text = note.content
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}