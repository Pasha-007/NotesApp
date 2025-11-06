package com.asg.notesapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.asg.notesapp.data.local.dao.NoteDao
import com.asg.notesapp.data.local.dao.UserDao
import com.asg.notesapp.data.local.entities.NoteEntity
import com.asg.notesapp.data.local.entities.UserEntity

@Database(
    entities = [
        UserEntity::class,
        NoteEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class NotesDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun userDao(): UserDao

    companion object{
        @Volatile
        private var INSTANCE: NotesDatabase? = null

        fun getDatabase(context: Context): NotesDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDatabase::class.java,
                    "notes_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}