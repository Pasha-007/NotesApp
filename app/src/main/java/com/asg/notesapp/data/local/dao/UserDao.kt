package com.asg.notesapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.asg.notesapp.data.local.entities.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM DIRECTORY_USER_DATA WHERE email = :userEmail")
    suspend fun getUserByEmail(userEmail: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM DIRECTORY_USER_DATA WHERE id = :id")
    suspend fun getUserById(id: Int): UserEntity?

}