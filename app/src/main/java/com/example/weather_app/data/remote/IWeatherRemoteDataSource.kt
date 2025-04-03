package com.example.weather_app.data.remote

import com.example.weather_app.models.ForecastResponse
import com.example.weather_app.models.WeatherDetails
import com.example.weather_app.models.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface IWeatherRemoteDataSource {
    suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        units: String,
        lang: String
    ): WeatherResponse

    suspend fun getForecast(
        lat: Double,
        lon: Double,
        units: String,
        lang: String
    ): ForecastResponse
}