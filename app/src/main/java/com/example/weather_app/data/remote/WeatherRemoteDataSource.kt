package com.example.weather_app.data.remote

import android.location.Location

class WeatherRemoteDataSource(
    private val services: WeatherApiServices,
) : IWeatherRemoteDataSource {
    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        units: String,
        lang: String
    ) = services.getCurrentWeather(lat, lon, units, lang)

    override suspend fun getForecast(
        lat: Double,
        lon: Double,
        units: String,
        lang: String
    ) = services.getForecast(lat, lon, units, lang)

}