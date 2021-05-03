package com.schaaf.authscreen.data.weather

import com.schaaf.authscreen.data.api.Api
import com.schaaf.authscreen.data.weather.model.CurrentWeatherResponse
import com.schaaf.authscreen.domain.weather.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val api: Api) : WeatherRepository {

    override suspend fun getCurrentWeather(cityId: Int): Flow<CurrentWeatherResponse?> = flow {
        emit(api.getCurrentWeather(cityId))
    }
}