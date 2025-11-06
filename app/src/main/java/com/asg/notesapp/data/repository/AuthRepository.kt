package com.asg.notesapp.data.repository

import android.content.Context
import com.asg.notesapp.data.local.dao.UserDao
import com.asg.notesapp.data.local.entities.UserEntity
import com.asg.notesapp.data.model.User

class AuthRepository(
    private val userDao: UserDao,
    private val context: Context
) {
    suspend fun signUp(name: String, email: String, password: String): Result<User> {
        return try {
            val existingUser = userDao.getUserByEmail(email)
            if (existingUser != null) {
                return Result.failure(Exception("User already exists"))
            }

            val passwordHash = password.hashCode().toString()

            val userEntity = UserEntity(
                userName = name,
                email = email,
                passwordHash = passwordHash
            )

            val userId = userDao.insertUser(userEntity)

            val user = User(
                id = userId.toInt(),
                name = name,
                email = email
            )

            saveUserSession(userId.toInt())

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun signIn(email: String, password: String): Result<User>{
        return try {
            val userEntity = userDao.getUserByEmail(email)
                ?: return Result.failure(Exception("User not found"))
            val passwordHash = password.hashCode().toString()
            if (userEntity.passwordHash != passwordHash) {
                return Result.failure(Exception("Invalid password"))
            }
            val user = User(
                id = userEntity.id,
                name = userEntity.userName,
                email = userEntity.email
            )
            saveUserSession(userEntity.id)
            Result.success(user)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    private fun saveUserSession(userId: Int) {
        val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        sharedPref.edit().putInt("user_id", userId).apply()
    }
    fun getCurrentUserId(): Int? {
        val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)
        return if (userId != -1) userId else null
    }
    fun logout() {
        val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()
    }
}