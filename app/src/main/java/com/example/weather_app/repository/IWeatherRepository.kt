package com.example.weather_app.repository

import com.example.weather_app.models.ForecastResponse
import com.example.weather_app.models.WeatherResponse

interface IWeatherRepository {
    suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        units: String,
        lang: String
    ): WeatherResponse?

    suspend fun getForecast(
        lat: Double,
        lon: Double,
        units: String,
        lang: String
    ): ForecastResponse?
}