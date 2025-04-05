package com.example.weather_app

import com.example.weather_app.data.local.IWeatherLocalDataSource
import com.example.weather_app.models.FavoriteLocation
import com.example.weather_app.models.WeatherAlert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeWeatherDataSource(private val alerts: MutableList<WeatherAlert> = mutableListOf()) :
    IWeatherLocalDataSource {

    override fun getAllFavoriteLocations(): Flow<List<FavoriteLocation>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertFavoriteLocation(location: FavoriteLocation): Long {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFavoriteLocation(location: FavoriteLocation): Int {
        TODO("Not yet implemented")
    }

    override fun insertAlert(alert: WeatherAlert): Long {
        alerts.add(alert)
        return 1L
    }

    override fun deleteAlert(id: Int): Int {
        for (alert in alerts) {
            if (alert.id == id) {
                alerts.remove(alert)
                return 1
            }
        }
        return 0
    }

    override fun getAlerts(): Flow<List<WeatherAlert>> = flowOf(alerts)

    override fun saveSetting(key: String, value: String) {
        TODO("Not yet implemented")
    }

    override fun getSetting(key: String, defValue: String): String? {
        TODO("Not yet implemented")
    }
}