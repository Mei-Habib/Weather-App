package com.example.weather_app.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "favorite_table")
data class FavoriteLocation(
    @PrimaryKey
    val name: String,
//    val location: LatLng,
//    val weatherResponse: WeatherResponse,
//    val forecastResponse: ForecastResponse
)