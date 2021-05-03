package com.schaaf.authscreen.data.api

import com.schaaf.authscreen.data.weather.model.CurrentWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("id")cityId: Int,
        @Query("units") units: String = "metric",
        @Query("appid")appid: String = "40fd6e4c882fd445c8677bfec987b80f"
    ):CurrentWeatherResponse?
}