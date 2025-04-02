package com.example.weather_app.data.local

import com.example.weather_app.models.FavoriteLocation
import com.example.weather_app.models.WeatherAlert
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

    override fun insertAlert(alert: WeatherAlert): Long {
        return weatherDao.insertAlert(alert)
    }

    override fun deleteAlert(id: Int): Int {
        return weatherDao.deleteAlertById(id)
    }

    override fun getAlerts(): Flow<List<WeatherAlert>> {
        return weatherDao.getAllAlerts()
    }
}