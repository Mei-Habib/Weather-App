package com.example.weather_app.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import androidx.annotation.RequiresApi
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.weather_app.R
import com.example.weather_app.models.ForecastResponse
import com.example.weather_app.receivers.AlarmReceiver
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.abs


@RequiresApi(Build.VERSION_CODES.O)
fun getDayFromTimestamp(unixTime: Int, timezoneOffset: Int): String {
    val instant = Instant.ofEpochSecond((unixTime + timezoneOffset).toLong())
    val formatter = DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy")
        .withLocale(Locale.getDefault())
        .withZone(ZoneId.of("UTC"))

    return formatter.format(instant)
}

@RequiresApi(Build.VERSION_CODES.O)
fun getDayFormTimestamp(timestamp: Int, context: Context): String {
    val date = Instant.ofEpochSecond(timestamp.toLong())
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    val today = LocalDate.now()
    val tomorrow = today.plusDays(1)

    return when (date) {


        today -> context.getString(R.string.today)
        tomorrow -> context.getString(R.string.tomorrow)
        else -> date.format(DateTimeFormatter.ofPattern("EEE"))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getHourFormTime(timestamp: Long): String {
    val time = Instant.ofEpochSecond(timestamp)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()

    return time.format(DateTimeFormatter.ofPattern("hh:mm a"))
}


fun ForecastResponse.getDaysForecast(): Map<Int, List<ForecastResponse.Item>> {
    val forecastMap = mutableMapOf<Int, MutableList<ForecastResponse.Item>>()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    for (item in list) {
        val date = dateFormat.format(Date(item.dt * 1000L)).replace("-", "").toInt()
        if (forecastMap[date] == null) {
            forecastMap[date] = mutableListOf()
        }
        forecastMap[date]?.add(item)
    }
    return forecastMap.mapValues { it.value.take(8) }
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.toMillis(): Long {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val localTime = LocalTime.parse(this, formatter)
    val now = LocalDate.now().atTime(localTime)
    return now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}




