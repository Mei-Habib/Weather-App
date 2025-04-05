package com.example.weather_app.data.local

import com.example.weather_app.data.local.room.WeatherDao
import com.example.weather_app.data.local.sharedpreferences.ISettingsSharedPreferences
import com.example.weather_app.models.FavoriteLocation
import com.example.weather_app.models.WeatherAlert
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSource(
    private val weatherDao: WeatherDao,
    private val sharedPreferences: ISettingsSharedPreferences
) : IWeatherLocalDataSource {
    override fun getAllFavoriteLocations(): Flow<List<FavoriteLocation>> =
        weatherDao.getAllFavoriteLocations()


    override suspend fun insertFavoriteLocation(location: FavoriteLocation) =
        weatherDao.insertFavoriteLocation(location)


    override suspend fun deleteFavoriteLocation(location: FavoriteLocation) =
        weatherDao.deleteFavoriteLocation(location)


    override fun insertAlert(alert: WeatherAlert) = weatherDao.insertAlert(alert)


    override fun deleteAlert(id: Int) = weatherDao.deleteAlertById(id)


    override fun getAlerts(): Flow<List<WeatherAlert>> {
        return weatherDao.getAllAlerts()
    }

    override fun saveSetting(key: String, value: String) = sharedPreferences.saveSetting(key, value)


    override fun getSetting(key: String, defValue: String) =
        sharedPreferences.getSetting(key, defValue)

}