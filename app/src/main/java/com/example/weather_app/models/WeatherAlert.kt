package com.example.weather_app.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson

@Entity(tableName = "alerts_table")
data class WeatherAlert(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val startDuration: String,
    val endDuration: String,
    val address: String
) {
    companion object {
        private val gson = Gson()
        fun fromJson(json: String?): WeatherAlert {
            return gson.fromJson(json, WeatherAlert::class.java)
        }

        fun WeatherAlert.toJson(): String {
            return gson.toJson(this)
        }
    }
}