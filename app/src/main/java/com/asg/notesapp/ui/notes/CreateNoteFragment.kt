package com.asg.notesapp.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.asg.notesapp.R
import com.asg.notesapp.databinding.FragmentCreateNoteBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateNoteFragment : Fragment() {

    private var _binding: FragmentCreateNoteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotesViewModel by viewModel()

    // Preview mode disables editing for a quick read-only look
    private var isPreviewMode = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        // Back
        binding.iconBack.setOnClickListener {
            hideKeyboard()
            findNavController().navigateUp()
        }

        // Save
        binding.iconSave.setOnClickListener {
            val title = binding.editTextTitle.text?.toString()?.trim().orEmpty()
            val content = binding.editTextContent.text?.toString()?.trim().orEmpty()

            var hasError = false
            if (title.isBlank()) {
                binding.editTextTitle.error = "Title is required"
                hasError = true
            } else {
                binding.editTextTitle.error = null
            }

            if (content.isBlank()) {
                binding.editTextContent.error = "Content is required"
                hasError = true
            } else {
                binding.editTextContent.error = null
            }

            if (hasError) return@setOnClickListener

            // Prevent double taps
            binding.iconSave.isEnabled = false
            hideKeyboard()

            viewModel.createNote(title, content)
            Toast.makeText(requireContext(), "Note saved", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }

        // Preview toggle (read-only mode)
        binding.iconVisibility.setOnClickListener {
            isPreviewMode = !isPreviewMode
            applyPreviewMode(isPreviewMode)
        }
    }

    private fun applyPreviewMode(preview: Boolean) {
        // Disable inputs when previewing
        binding.editTextTitle.isEnabled = !preview
        binding.editTextContent.isEnabled = !preview

        // Visually indicate preview
        binding.editTextTitle.alpha = if (preview) 0.7f else 1f
        binding.editTextContent.alpha = if (preview) 0.7f else 1f

        // Hide keyboard when entering preview
        if (preview) hideKeyboard()

        // Swap icon if alt resource exists; otherwise, keep same icon
        val altIconId = resources.getIdentifier(
            "ic_visibility_off",
            "drawable",
            requireContext().packageName
        )
        if (altIconId != 0) {
            binding.iconVisibility.setImageResource(if (preview) altIconId else R.drawable.ic_visibility)
        }
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