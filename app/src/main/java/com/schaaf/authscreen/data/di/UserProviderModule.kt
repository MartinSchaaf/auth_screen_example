package com.schaaf.authscreen.data.di

import com.schaaf.authscreen.data.user.UserRepositoryImpl
import com.schaaf.authscreen.data.user.database.DaoUsers
import com.schaaf.authscreen.domain.user.UserRepository
import com.schaaf.authscreen.domain.user.UserUseCase
import com.schaaf.authscreen.domain.user.UserUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserProviderModule {

    @Provides
    @Singleton
    fun provideUserRepository(daoUsers: DaoUsers): UserRepository = UserRepositoryImpl(daoUsers)

    @Provides
    @Singleton
    fun provideUserUseCase(repository: UserRepository): UserUseCase = UserUseCaseImpl(repository)
}