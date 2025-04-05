package com.example.weather_app.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.weather_app.R
import com.example.weather_app.components.BottomNavBar
import com.example.weather_app.worker.WeatherWorkManager
import com.example.weather_app.models.ForecastResponse
import com.example.weather_app.models.WeatherAlert
import com.example.weather_app.models.WeatherAlert.Companion.toJson
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


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

fun getLocationFromAddress(context: Context, address: String): Pair<Double, Double>? {
    return try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocationName(address, 1)

        if (!addresses.isNullOrEmpty()) {
            val location = addresses[0]
            Pair(location.latitude, location.longitude)
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun formatTime(timeMillis: Long): String {
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return sdf.format(Date(timeMillis))
}

@RequiresApi(Build.VERSION_CODES.O)
fun scheduleWeatherAlert(workManager: WorkManager, alert: WeatherAlert) {
    val delay = alert.startDuration.toMillis() - System.currentTimeMillis()
    val jAlert = alert.toJson()
    val inputData = Data.Builder()
        .putString("alert", jAlert)
        .build()

    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    if (delay > 0) {
        val workRequest = OneTimeWorkRequestBuilder<WeatherWorkManager>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .setConstraints(constraints)
            .build()

        workManager.enqueue(workRequest)
    } else {
        Log.e("WeatherAlert", "Invalid alert time: ${alert.startDuration}")
    }
}

fun restartActivity(context: Context) {
    val intent = (context as? Activity)?.intent
    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
    (context as? Activity)?.finish()
}

fun convertTemperature(celsius: Int, unit: String): Int {
    return when (unit) {
        "°C", "°س" -> celsius
        "°K", "°ك" -> (celsius + 273.15).toInt()
        "°F", "°ف" -> ((celsius * 9 / 5) + 32)
        else -> celsius
    }
}

fun convertSpeed(value: Double, unit: String): Double {
    return when (unit) {
        "mph", "ميل/س" -> value * 2.23694
        "m/s", "م/ث" -> value
        else -> value
    }
}


