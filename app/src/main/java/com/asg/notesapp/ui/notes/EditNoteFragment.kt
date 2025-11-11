package com.asg.notesapp.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.asg.notesapp.R
import com.asg.notesapp.data.model.Note
import com.asg.notesapp.databinding.FragmentEditNoteBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditNoteFragment : Fragment() {

    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotesViewModel by viewModel()

    private var isPreviewMode = false
    private var noteId: Int = -1
    private var currentNote: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Read noteId from arguments
        noteId = arguments?.getInt("noteId", -1) ?: -1
        if (noteId == -1) {
            Toast.makeText(requireContext(), "Invalid note", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
            return
        }

        // Toolbar: back arrow
        binding.toolbar.setNavigationOnClickListener {
            hideKeyboard()
            findNavController().navigateUp()
        }

        // Toolbar menu (Preview / Save)
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_preview -> {
                    togglePreview()
                    true
                }
                R.id.action_save -> {
                    saveChanges()
                    true
                }
                else -> false
            }
        }

        // Load & observe the note
        viewModel.loadNoteById(noteId)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentNote.collect { note ->
                    note?.let {
                        currentNote = it
                        // Pre-fill fields once data arrives
                        binding.editTextTitle.setText(it.title)
                        binding.editTextContent.setText(it.content)
                    }
                }
            }
        }
    }

    private fun togglePreview() {
        isPreviewMode = !isPreviewMode

        binding.editTextTitle.isEnabled = !isPreviewMode
        binding.editTextContent.isEnabled = !isPreviewMode
        binding.editTextTitle.alpha = if (isPreviewMode) 0.7f else 1f
        binding.editTextContent.alpha = if (isPreviewMode) 0.7f else 1f
        if (isPreviewMode) hideKeyboard()

        // Swap preview icon if you have ic_visibility_off
        val previewItem = binding.toolbar.menu.findItem(R.id.action_preview)
        val offId = resources.getIdentifier("ic_visibility_off", "drawable", requireContext().packageName)
        if (offId != 0) {
            previewItem?.setIcon(if (isPreviewMode) offId else R.drawable.ic_visibility)
        }
    }

    private fun saveChanges() {
        val original = currentNote
        if (original == null) {
            Toast.makeText(requireContext(), "Note not loaded yet", Toast.LENGTH_SHORT).show()
            return
        }

        val newTitle = binding.editTextTitle.text?.toString()?.trim().orEmpty()
        val newContent = binding.editTextContent.text?.toString()?.trim().orEmpty()

        var hasError = false
        if (newTitle.isBlank()) {
            binding.editTextTitle.error = "Title is required"
            hasError = true
        } else binding.editTextTitle.error = null

        if (newContent.isBlank()) {
            binding.editTextContent.error = "Content is required"
            hasError = true
        } else binding.editTextContent.error = null

        if (hasError) return

        // Prevent double tapping save
        binding.toolbar.menu.findItem(R.id.action_save)?.isEnabled = false

        hideKeyboard()
        viewModel.updateNote(original, newTitle, newContent)
        Toast.makeText(requireContext(), "Note updated", Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService<InputMethodManager>()
        imm?.hideSoftInputFromWindow(binding.root.windowToken, 0)
        binding.editTextTitle.clearFocus()
        binding.editTextContent.clearFocus()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}