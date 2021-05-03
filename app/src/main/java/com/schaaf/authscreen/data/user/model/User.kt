package com.schaaf.authscreen.data.user.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "users", indices = [Index(value = ["login"], unique = true)])
data class User(
    val login: String,
    val password: String
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}