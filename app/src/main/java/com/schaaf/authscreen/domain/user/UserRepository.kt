package com.schaaf.authscreen.domain.user

import com.schaaf.authscreen.data.user.model.User

interface UserRepository {

    suspend fun getUser(login: String, password: String): User?

    suspend fun addUser(user: User): Long
}