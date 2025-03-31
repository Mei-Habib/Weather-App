package com.example.weather_app.data.local

import com.example.weather_app.models.FavoriteLocation
import kotlinx.coroutines.flow.Flow

interface IWeatherLocalDataSource {
    fun getAllFavoriteLocations(): Flow<List<FavoriteLocation>>
    suspend fun insertFavoriteLocation(location: FavoriteLocation): Long
    suspend fun deleteFavoriteLocation(location: FavoriteLocation): Int
}