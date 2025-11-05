package com.asg.notesapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DIRECTORY_NOTE_DATA")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var title: String? = null,
    var content: String? = null,
    var createdAt: Long? = System.currentTimeMillis(),
    var updatedAt: Long? = System.currentTimeMillis(),
    var userId: Int? = null,
)