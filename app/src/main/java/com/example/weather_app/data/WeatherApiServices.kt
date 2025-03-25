package com.example.weather_app.data

import com.example.weather_app.models.ForecastResponse
import com.example.weather_app.models.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiServices {

    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("lang") lang: String
    ): WeatherResponse

    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("lang") lang: String
    ): ForecastResponse

}