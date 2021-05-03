package com.schaaf.authscreen.domain.weather

import com.schaaf.authscreen.data.weather.model.CurrentWeatherResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface WeatherUseCase {

    suspend fun getCurrentWeather(cityId: Int): Flow<CurrentWeatherResponse?>
}

class WeatherUseCaseImpl @Inject constructor(private val weatherRepository: WeatherRepository) : WeatherUseCase {

    override suspend fun getCurrentWeather(cityId: Int): Flow<CurrentWeatherResponse?> =
        weatherRepository.getCurrentWeather(cityId)
}