package com.schaaf.authscreen.data.user.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.schaaf.authscreen.data.user.model.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUsersDao(): DaoUsers
}