package com.example.weather_app.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weather_app.enums.AlertType

@Entity(tableName = "alerts_table")
data class WeatherAlert(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val startTime: String,
    val endTime: String,
    val type: AlertType
)