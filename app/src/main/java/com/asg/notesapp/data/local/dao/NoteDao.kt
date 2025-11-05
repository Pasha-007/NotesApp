package com.asg.notesapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.asg.notesapp.data.local.entities.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM DIRECTORY_NOTE_DATA WHERE userId = :userId ORDER BY updatedAt DESC")
    fun getAllNotes(userId: Int): Flow<List<NoteEntity>>

    @Query("SELECT * FROM DIRECTORY_NOTE_DATA WHERE id = :noteId")
    suspend fun getNoteById(noteId: Int): NoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(notes: NoteEntity)

    @Delete
    suspend fun deleteNote(log: NoteEntity)

    @Query("DELETE FROM DIRECTORY_NOTE_DATA WHERE id = :noteId")
    suspend fun deleteNoteById(noteId: Int)

}