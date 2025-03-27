package com.example.weather_app.repository

import com.example.weather_app.data.local.IWeatherLocalDataSource
import com.example.weather_app.data.remote.IWeatherRemoteDataSource

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