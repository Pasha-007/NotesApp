package com.asg.notesapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DIRECTORY_USER_DATA")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var userName: String,
    var email: String,
    val passwordHash: String,
    var createdAt: Long? = System.currentTimeMillis()
)