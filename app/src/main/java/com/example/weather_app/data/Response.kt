package com.example.weather_app.data

sealed class Response<out T> {
    data object Loading : Response<Nothing>()
    data class Success<out T>(val data: T) : Response<T>()
    data class Failure(val exception: String) : Response<Nothing>()
}