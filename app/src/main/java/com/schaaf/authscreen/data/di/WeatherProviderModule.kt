package com.schaaf.authscreen.data.di

import com.schaaf.authscreen.data.api.Api
import com.schaaf.authscreen.data.weather.WeatherRepositoryImpl
import com.schaaf.authscreen.domain.weather.WeatherRepository
import com.schaaf.authscreen.domain.weather.WeatherUseCase
import com.schaaf.authscreen.domain.weather.WeatherUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeatherProviderModule {

    @Provides
    @Singleton
    fun provideWeatherRepository(api: Api): WeatherRepository = WeatherRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideWeatherUseCase(repository: WeatherRepository): WeatherUseCase = WeatherUseCaseImpl(repository)
}