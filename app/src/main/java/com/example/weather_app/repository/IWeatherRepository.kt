package com.example.weather_app.repository

import com.example.weather_app.models.FavoriteLocation
import com.example.weather_app.models.ForecastResponse
import com.example.weather_app.models.WeatherResponse
import kotlinx.coroutines.flow.Flow

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

    suspend fun getFavoriteLocations(): Flow<List<FavoriteLocation>>

    suspend fun insertFavoriteLocation(location: FavoriteLocation): Long

    suspend fun deleteFavoriteLocation(location: FavoriteLocation): Int
}