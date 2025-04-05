package com.example.weather_app.data.local

import com.example.weather_app.models.FavoriteLocation
import com.example.weather_app.models.WeatherAlert
import kotlinx.coroutines.flow.Flow

interface IWeatherLocalDataSource {
    fun getAllFavoriteLocations(): Flow<List<FavoriteLocation>>
    suspend fun insertFavoriteLocation(location: FavoriteLocation): Long
    suspend fun deleteFavoriteLocation(location: FavoriteLocation): Int

    fun insertAlert(alert: WeatherAlert): Long
    fun deleteAlert(id: Int): Int
    fun getAlerts(): Flow<List<WeatherAlert>>

    fun saveSetting(key: String, value: String)
    fun getSetting(key: String, defValue: String): String?
}