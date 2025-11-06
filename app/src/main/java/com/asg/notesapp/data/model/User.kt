package com.asg.notesapp.data.model

import com.asg.notesapp.data.local.entities.UserEntity

data class User(
    val id: Int = 0,
    val name: String,
    val email: String
)
// Entity to Domain
fun UserEntity.toUser() = User(
    id = id,
    name = userName,
    email = email)

