package com.schaaf.authscreen.data.di

import android.content.Context
import androidx.room.Room
import com.schaaf.authscreen.data.user.database.AppDatabase
import com.schaaf.authscreen.data.user.database.DaoUsers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseProviderModule {

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app_database").build()

    @Provides
    @Singleton
    fun provideFavoriteChannelsDao(appDatabase: AppDatabase): DaoUsers =
        appDatabase.getUsersDao()
}