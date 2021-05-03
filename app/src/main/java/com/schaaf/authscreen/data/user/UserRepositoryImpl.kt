package com.schaaf.authscreen.data.user

import com.schaaf.authscreen.data.user.database.DaoUsers
import com.schaaf.authscreen.data.user.model.User
import com.schaaf.authscreen.domain.user.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val usersDao: DaoUsers) : UserRepository {

    override suspend fun getUser(login: String, password: String): User? =
        usersDao.getUser(login, password)

    override suspend fun addUser(user: User): Long = usersDao.addUser(user)
}