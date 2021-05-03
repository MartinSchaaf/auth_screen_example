package com.schaaf.authscreen.data.user.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.schaaf.authscreen.data.user.model.User

@Dao
interface DaoUsers {

    @Query("SELECT * FROM users WHERE login=:login OR password=:password")
    suspend fun getUser(login: String, password: String): User?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User): Long
}