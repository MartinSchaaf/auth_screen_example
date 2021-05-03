package com.schaaf.authscreen.domain.user

import com.schaaf.authscreen.data.user.model.User
import javax.inject.Inject

interface UserUseCase {

    suspend fun getUser(login: String, password: String): User?

    suspend fun addUser(user: User): Long
}

class UserUseCaseImpl @Inject constructor(private val userRepository: UserRepository) : UserUseCase{

    override suspend fun getUser(login: String, password: String): User? =
        userRepository.getUser(login, password)

    override suspend fun addUser(user: User): Long = userRepository.addUser(user)
}