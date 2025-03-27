package com.example.weather_app.data.remote

import com.example.weather_app.models.WeatherResponse

sealed class Response<out T> {
    data object Loading : Response<Nothing>()
    data class Success<T>(val response: T) : Response<T>()
    data class Failure(val err: Throwable) : Response<Nothing>()

}