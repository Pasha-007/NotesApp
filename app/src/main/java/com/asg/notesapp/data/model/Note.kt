package com.asg.notesapp.data.model

import com.asg.notesapp.data.local.entities.NoteEntity

data class Note(
    val id: Int = 0,
    val title: String? = null,
    val content: String? = null,
    val createdAt: Long? = System.currentTimeMillis(),
    val updatedAt: Long? = System.currentTimeMillis(),
    val userId: Int? = null
)

// Entity to Domain
fun NoteEntity.toNote() = Note(
    id = id,
    title = title,
    content = content,
    createdAt = createdAt,
    updatedAt = updatedAt,
    userId = userId)

// Domain to Entity
fun Note.toEntity() = NoteEntity(
    id = id,
    title = title,
    content = content,
    createdAt = createdAt,
    updatedAt = updatedAt,
    userId = userId)