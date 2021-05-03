package com.schaaf.authscreen.data.di

import android.content.Context
import com.schaaf.authscreen.data.parse_exception.ParseExceptionRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.schaaf.authscreen.domain.parse_exception.ParseExceptionRepository
import com.schaaf.authscreen.domain.parse_exception.ParseExceptionUseCase
import com.schaaf.authscreen.domain.parse_exception.ParseExceptionUseCaseImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ParseExceptionProviderModule {

    @Provides
    @Singleton
    fun provideParseExceptionRepository(@ApplicationContext context: Context): ParseExceptionRepository =
        ParseExceptionRepositoryImpl(context)

    @Provides
    @Singleton
    fun provideFavoriteChannelsUseCase(repo: ParseExceptionRepository): ParseExceptionUseCase =
        ParseExceptionUseCaseImpl(repo)
}