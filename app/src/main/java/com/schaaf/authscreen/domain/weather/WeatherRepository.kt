package com.schaaf.authscreen.domain.weather

import com.schaaf.authscreen.data.weather.model.CurrentWeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getCurrentWeather(cityId: Int): Flow<CurrentWeatherResponse?>
}