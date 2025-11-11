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

        // Back arrow in the toolbar
        binding.toolbar.setNavigationOnClickListener {
            hideKeyboard()
            findNavController().navigateUp()
        }

        // Toolbar menu actions
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_preview -> {
                    applyPreviewMode(toggle = true)
                    true
                }
                R.id.action_save -> {
                    saveNote()
                    true
                }
                else -> false
            }
        }
    }

    private fun saveNote() {
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

        if (hasError) return

        // UX: prevent double taps on Save menu item
        val saveItem = binding.toolbar.menu.findItem(R.id.action_save)
        saveItem?.isEnabled = false

        hideKeyboard()
        viewModel.createNote(title, content)
        Toast.makeText(requireContext(), "Note saved", Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }

    private fun applyPreviewMode(toggle: Boolean = false) {
        if (toggle) isPreviewMode = !isPreviewMode

        // Disable/enable inputs
        binding.editTextTitle.isEnabled = !isPreviewMode
        binding.editTextContent.isEnabled = !isPreviewMode

        // Visual hint
        binding.editTextTitle.alpha = if (isPreviewMode) 0.7f else 1f
        binding.editTextContent.alpha = if (isPreviewMode) 0.7f else 1f

        if (isPreviewMode) hideKeyboard()

        // Swap the preview icon if you have ic_visibility_off
        val previewItem = binding.toolbar.menu.findItem(R.id.action_preview)
        val offId = resources.getIdentifier("ic_visibility_off", "drawable", requireContext().packageName)
        if (offId != 0) {
            previewItem?.setIcon(if (isPreviewMode) offId else R.drawable.ic_visibility)
        }
        // Optional: also change the title
        // previewItem?.title = if (isPreviewMode) "Edit" else "Preview"
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
