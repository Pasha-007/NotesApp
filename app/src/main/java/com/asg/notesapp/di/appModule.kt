@file:Suppress("DEPRECATION")

package com.asg.notesapp.di

import androidx.room.Room
import com.asg.notesapp.data.local.database.NotesDatabase
import com.asg.notesapp.data.repository.AuthRepository
import com.asg.notesapp.data.repository.NotesRepository
import com.asg.notesapp.ui.auth.AuthViewModel
import com.asg.notesapp.ui.notes.NotesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel // ← Make sure this import
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            NotesDatabase::class.java,
            "notesDatabase"
        ).build()
    }

    single { get<NotesDatabase>().noteDao() }
    single { get<NotesDatabase>().userDao() }

    single { AuthRepository(get(), androidContext()) }
    single { NotesRepository(get()) }

    viewModel { AuthViewModel(get())}
    viewModel { NotesViewModel(get(), get()) }

}