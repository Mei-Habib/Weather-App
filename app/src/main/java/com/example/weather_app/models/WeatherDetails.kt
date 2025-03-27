package com.example.weather_app.models

data class WeatherDetails(
    val weather: WeatherResponse,
    val forecast: ForecastResponse
)