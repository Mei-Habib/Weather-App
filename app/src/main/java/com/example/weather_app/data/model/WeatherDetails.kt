package com.example.weather_app.data.model

data class WeatherDetails(
    val temp: String,
    val humidity: String,
    val windSpeed: String,
    val uv: String,
    val pressure: String,
    val clouds: String
) {
    companion object {
        val weatherDetails = WeatherDetails("13", "62", "8", "0", "1027", "100")
    }
}