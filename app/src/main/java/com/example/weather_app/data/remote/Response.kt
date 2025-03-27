package com.example.weather_app.data.remote

import com.example.weather_app.models.WeatherResponse

sealed class Response {
    data object Loading : Response()
    data class Success(val response: WeatherResponse) : Response()
    data class Failure(val err: Throwable) : Response()
}