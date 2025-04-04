package com.example.weather_app.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun String.toMillis(): Long {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val localTime = LocalTime.parse(this, formatter)
    val now = LocalDate.now().atTime(localTime)
    return now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}