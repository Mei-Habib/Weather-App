package com.example.weather_app.data.local

import com.example.weather_app.models.FavoriteLocation
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSource(private val weatherDao: WeatherDao) : IWeatherLocalDataSource {
    override fun getAllFavoriteLocations(): Flow<List<FavoriteLocation>> {
        return weatherDao.getAllFavoriteLocations()
    }

    override suspend fun insertFavoriteLocation(location: FavoriteLocation): Long {
        return weatherDao.insertFavoriteLocation(location)
    }

    override suspend fun deleteFavoriteLocation(location: FavoriteLocation): Int {
        return weatherDao.deleteFavoriteLocation(location)
    }
}