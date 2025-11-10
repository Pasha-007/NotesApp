package com.asg.notesapp

import android.app.Application
import com.asg.notesapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class NotesApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@NotesApplication)
            modules(appModule)
        }
    }
}