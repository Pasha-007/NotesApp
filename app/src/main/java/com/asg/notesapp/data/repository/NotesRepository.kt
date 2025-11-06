package com.asg.notesapp.data.repository

import com.asg.notesapp.data.local.dao.NoteDao
import com.asg.notesapp.data.local.dao.UserDao
import com.asg.notesapp.data.local.entities.NoteEntity
import com.asg.notesapp.data.model.Note
import com.asg.notesapp.data.model.toEntity
import com.asg.notesapp.data.model.toNote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotesRepository (
    private val noteDao: NoteDao
    ) {
    fun getAllNotes(userId: Int): Flow<List<Note>> {
        return noteDao.getAllNotes(userId).map { list ->
            list.map { it.toNote() }
        }
    }
    suspend fun getNoteById(noteId: Int): Note? {
        return noteDao.getNoteById(noteId)?.toNote()
    }
    suspend fun insertNote(note: Note): Long {
        return noteDao.insertNote(note.toEntity())
    }
    suspend fun updateNote(note: Note) {
        val updatedNote = note.copy(updatedAt = System.currentTimeMillis())
        noteDao.updateNote(updatedNote.toEntity())
    }
    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note.toEntity())
    }
}