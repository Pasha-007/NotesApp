package com.asg.notesapp.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asg.notesapp.data.model.Note
import com.asg.notesapp.data.repository.AuthRepository
import com.asg.notesapp.data.repository.NotesRepository
import com.asg.notesapp.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotesViewModel(
    private val repository: NotesRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _notesState = MutableStateFlow<UiState<List<Note>>>(UiState.Idle)
    val notesState: StateFlow<UiState<List<Note>>> = _notesState.asStateFlow()

    private val _currentNote = MutableStateFlow<Note?>(null)
    val currentNote: StateFlow<Note?> = _currentNote.asStateFlow()

    private val userId: Int
        get() = authRepository.getCurrentUserId() ?: 0

    init {
        loadNotes()
    }

    fun loadNotes() {
        viewModelScope.launch {
            _notesState.value = UiState.Loading
            try {
                repository.getAllNotes(userId).collect { notes ->
                    _notesState.value = UiState.Success(notes)
                }
            } catch (e: Exception) {
                _notesState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadNoteById(noteId: Int) {
        viewModelScope.launch {
            try {
                val note = repository.getNoteById(noteId)
                _currentNote.value = note
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun createNote(title: String, content: String) {
        viewModelScope.launch {
            try {
                val note = Note(
                    title = title,
                    content = content,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    userId = userId
                )
                repository.insertNote(note)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun updateNote(note: Note, newTitle: String, newContent: String) {
        viewModelScope.launch {
            try {
                val updatedNote = note.copy(
                    title = newTitle,
                    content = newContent,
                    updatedAt = System.currentTimeMillis()
                )
                repository.updateNote(updatedNote)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            try {
                repository.deleteNote(note)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}