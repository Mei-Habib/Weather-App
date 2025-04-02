package com.example.weather_app.repository

import com.example.weather_app.data.local.IWeatherLocalDataSource
import com.example.weather_app.data.remote.IWeatherRemoteDataSource
import com.example.weather_app.models.FavoriteLocation
import com.example.weather_app.models.WeatherAlert
import kotlinx.coroutines.flow.Flow

class WeatherRepository private constructor(
    private val remoteDataSource: IWeatherRemoteDataSource,
    private val localDataSource: IWeatherLocalDataSource
) : IWeatherRepository {
    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        units: String,
        lang: String
    ) = remoteDataSource.getCurrentWeather(lat, lon, units, lang)

    override suspend fun getForecast(
        lat: Double,
        lon: Double,
        units: String,
        lang: String
    ) = remoteDataSource.getForecast(lat, lon, units, lang)

    override suspend fun getFavoriteLocations(): Flow<List<FavoriteLocation>> {
        return localDataSource.getAllFavoriteLocations()
    }

    override suspend fun insertFavoriteLocation(location: FavoriteLocation): Long {
        return localDataSource.insertFavoriteLocation(location)
    }

    override suspend fun deleteFavoriteLocation(location: FavoriteLocation): Int {
        return localDataSource.deleteFavoriteLocation(location)
    }

    override fun insertAlert(alert: WeatherAlert): Long {
        return localDataSource.insertAlert(alert)
    }

    override fun deleteAlert(id: Int): Int {
        return localDataSource.deleteAlert(id)
    }

    override fun getAlerts(): Flow<List<WeatherAlert>> {
        return localDataSource.getAlerts()
    }


    companion object {
        private var repository: WeatherRepository? = null
        fun getInstance(
            remoteDataSource: IWeatherRemoteDataSource,
            localDataSource: IWeatherLocalDataSource
        ): WeatherRepository {

            return repository ?: synchronized(this) {
                val repo = WeatherRepository(remoteDataSource, localDataSource)
                repository = repo
                repo
            }
        }
    }
}