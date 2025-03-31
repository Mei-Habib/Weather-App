package com.example.weather_app.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson

@Entity(tableName = "favorite_table")
data class FavoriteLocation(
    @PrimaryKey
    val location: LatLng,
    val counter: String,
    val city: String,
    val weather: WeatherDetails
) {
    companion object {
        private val gson = Gson()
        fun fromJson(json: String): FavoriteLocation {
            return gson.fromJson(json, FavoriteLocation::class.java)
        }

        fun FavoriteLocation.toJson(): String {
            return gson.toJson(this)
        }
    }
}